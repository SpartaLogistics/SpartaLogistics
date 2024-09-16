package com.sparta.logistics.client.order.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.logistics.client.order.dto.OrderResponseDto;
import com.sparta.logistics.client.order.model.Order;
import com.sparta.logistics.client.order.model.QOrder;
import com.sparta.logistics.client.order.model.QOrderProduct;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public OrderRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public OrderResponseDto findByOrderIdWithOrderProducts(UUID orderId) {
        QOrder qOrder = QOrder.order;
        QOrderProduct qOrderProduct = QOrderProduct.orderProduct;

        Order order = jpaQueryFactory
                .selectFrom(qOrder)
                .leftJoin(qOrder.orderProducts, qOrderProduct)
                .where(qOrder.orderId.eq(orderId)
                        .and(qOrder.isDeleted.eq(false)))
                .fetchOne();

        return OrderResponseDto.of(order);
    }


}
