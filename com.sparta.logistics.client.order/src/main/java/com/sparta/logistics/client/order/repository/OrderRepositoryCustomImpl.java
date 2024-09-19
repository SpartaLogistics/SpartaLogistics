package com.sparta.logistics.client.order.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.logistics.client.order.common.exception.OrderProcException;
import com.sparta.logistics.client.order.dto.*;
import com.sparta.logistics.client.order.model.*;
import com.sparta.logistics.common.type.ApiResultError;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public OrderRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public OrderResponseDto findByOrderIdWithOrderProducts(UUID orderId) throws OrderProcException {
        QOrder qOrder = QOrder.order;
        QOrderProduct qOrderProduct = QOrderProduct.orderProduct;
        QDelivery qDelivery = QDelivery.delivery;
        QDeliveryPath qDeliveryPath = QDeliveryPath.deliveryPath;

        Tuple result = jpaQueryFactory
                .select(qOrder, qDelivery)
                .from(qOrder)
                .leftJoin(qOrder.orderProducts, qOrderProduct)
                .leftJoin(qDelivery).on(qOrder.orderId.eq(qDelivery.orderId))
                .leftJoin(qDelivery.deliveryPathsList, qDeliveryPath)
                .where(qOrder.orderId.eq(orderId)
                        .and(qOrder.isDeleted.eq(false)))
                .fetchFirst();

        if(result == null) {
            throw new OrderProcException(ApiResultError.ORDER_NO_EXIST);
        }

        Order order = result.get(qOrder);
        Delivery delivery = result.get(qDelivery);

        List<DeliveryPath> deliveryPaths = jpaQueryFactory
                .select(qDeliveryPath)
                .from(qDeliveryPath)
                .where(qDeliveryPath.delivery.eq(delivery))
                .fetch();

        return OrderResponseDto.of(order, order.getOrderProducts(), delivery, deliveryPaths);
    }

    // 검색 및 페이징 메서드
    @Override
    public Page<OrderResponseDto> searchOrders(OrderSearchCriteria criteria, Pageable pageable) {
        QOrder order = QOrder.order;
        QOrderProduct orderProduct = QOrderProduct.orderProduct;
        QDelivery delivery = QDelivery.delivery;
        QDeliveryPath deliveryPath = QDeliveryPath.deliveryPath;

        List<OrderResponseDto> orders = jpaQueryFactory
                .select(Projections.fields(OrderResponseDto.class,
                        order.orderId,
                        order.senderId,
                        order.receiverId,
                        order.deliveryId,
                        order.status,
                        order.isDeleted,
                        order.remark,
                        Projections.fields(DeliveryResponseDto.class,
                                delivery.deliveryId,
                                delivery.orderId,
                                delivery.status,
                                delivery.departureId,
                                delivery.arrivalId,
                                delivery.address,
                                delivery.receiver,
                                delivery.slackId,
                                delivery.isDeleted,
                                delivery.totalDistance,
                                delivery.totalTime
                        ).as("delivery")
                ))
                .from(order)
                .leftJoin(delivery).on(order.deliveryId.eq(delivery.deliveryId))
                .where(
                        (criteria.getSenderId() != null ? order.senderId.eq(criteria.getSenderId()) : null),
                        (criteria.getReceiverId() != null ? order.receiverId.eq(criteria.getReceiverId()) : null),
                        (criteria.getStatus() != null ? order.status.eq(criteria.getStatus()) : null),
                        (criteria.getIsDeleted() != null ? order.isDeleted.eq(criteria.getIsDeleted()) : null)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<UUID> orderIds = orders.stream().map(OrderResponseDto::getOrderId).collect(Collectors.toList());
        List<UUID> deliveryIds = orders.stream().map(OrderResponseDto::getDeliveryId).collect(Collectors.toList());

        List<OrderProductResponseDto> products = jpaQueryFactory
                .select(Projections.fields(OrderProductResponseDto.class,
                        orderProduct.orderProductId,
                        orderProduct.orderId,
                        orderProduct.productId,
                        orderProduct.quantity,
                        orderProduct.isDeleted))
                .from(orderProduct)
                .where(orderProduct.orderId.in(orderIds))
                .fetch();

        List<DeliveryPathResponseDto> deliveryPaths = jpaQueryFactory
                .select(Projections.fields(DeliveryPathResponseDto.class,
                        deliveryPath.deliveryPathId,
                        deliveryPath.delivery.deliveryId,
                        deliveryPath.status,
                        deliveryPath.sequence,
                        deliveryPath.departureId,
                        deliveryPath.arrivalId,
                        deliveryPath.expectedDistance,
                        deliveryPath.expectedTime,
                        deliveryPath.actualDistance,
                        deliveryPath.actualTime,
                        deliveryPath.isDeleted))
                .from(deliveryPath)
                .where(deliveryPath.delivery.deliveryId.in(deliveryIds))
                .fetch();

        Map<UUID, List<OrderProductResponseDto>> productsMap = products.stream()
                .collect(Collectors.groupingBy(OrderProductResponseDto::getOrderId));
        Map<UUID, List<DeliveryPathResponseDto>> deliveryPathsMap = deliveryPaths.stream()
                .collect(Collectors.groupingBy(DeliveryPathResponseDto::getDeliveryId));

        orders.forEach(orderResponseDto -> {
            orderResponseDto.setProducts(productsMap.get(orderResponseDto.getOrderId()));
            orderResponseDto.setDeliveryPaths(deliveryPathsMap.get(orderResponseDto.getDeliveryId()));
        });

        long total = jpaQueryFactory.selectFrom(order).fetchCount();

        return new PageImpl<>(orders, pageable, total);
    }
}
