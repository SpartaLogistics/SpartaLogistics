package com.sparta.logistics.client.order.service;

import com.sparta.logistics.client.order.common.exception.OrderProcException;
import com.sparta.logistics.client.order.common.type.OrderStatus;
import com.sparta.logistics.client.order.dto.OrderRequestDto;
import com.sparta.logistics.client.order.dto.OrderResponseDto;
import com.sparta.logistics.client.order.dto.OrderSearchCriteria;
import com.sparta.logistics.client.order.dto.OrderSearchDto;
import com.sparta.logistics.client.order.model.Order;
import com.sparta.logistics.client.order.repository.OrderRepository;
import com.sparta.logistics.common.type.ApiResultError;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    // 주문 생성
    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) throws OrderProcException {
        // TODO 데이터 검증
        // 1. 발송처 확인
        // 2. 배송처 확인

        Order order = Order.OrderCreateBuilder()
                .orderDto(orderRequestDto)
                .build();
        return OrderResponseDto.of(orderRepository.save(order));
    }

    // 주문 목록 조회
    public Page<Order> getOrderList(OrderSearchDto orderSearchDto, Pageable pageable)  {
        // TODO 관리자 외 자신의 주문만 조회 가능
        return orderRepository.findAllByIsDeletedFalse(pageable);
    }

    // 주문 상세 조회
    public OrderResponseDto getOrder(UUID orderId) throws OrderProcException {
        // 주문 확인
        Order order = orderRepository.findByOrderIdAndIsDeletedFalse(orderId).orElseThrow( ()->
                // 주문이 존재하지 않습니다.
                new OrderProcException(ApiResultError.ORDER_NO_EXIST)
        );

        // 관리자 외 자신의 주문만 조회 가능
        return OrderResponseDto.of(order);
    }

    // 주문 삭제
    @Transactional
    public void deleteOrder(UUID orderId, OrderStatus orderStatus, String userId) throws OrderProcException {
        // 주문 확인
        Order order = orderRepository.findByOrderIdAndIsDeletedFalse(orderId).orElseThrow( ()->
            // 주문이 존재하지 않습니다.
            new OrderProcException(ApiResultError.ORDER_NO_EXIST)
        );

        Order deleteOrder = Order.OrderUpdateStatusBuilder()
                .order(order)
                .orderStatus(orderStatus)
                .isDeleted(true)
                .build();
        deleteOrder.softDelete(userId);
        log.info("!!!!!!!!!!!Deleted order: {}", order);
        orderRepository.save(deleteOrder);
    }

    @Transactional
    public OrderResponseDto updateOrder(OrderRequestDto orderRequestDto) throws OrderProcException {
        UUID orderId = orderRequestDto.getOrderId();
        // 주문 확인
        Order order = orderRepository.findByOrderIdAndIsDeletedFalse(orderId).orElseThrow( ()->
                // 주문이 존재하지 않습니다.
                new OrderProcException(ApiResultError.ORDER_NO_EXIST)
        );

        Order updatedOrder = Order.OrderUpdateBuilder()
                .orderDto(orderRequestDto)
                .order(order)
                .build();
        return OrderResponseDto.of(orderRepository.save(updatedOrder));

    }

    public OrderResponseDto getOrderWithOrderProducts(UUID orderId) throws OrderProcException {
        return orderRepository.findByOrderIdWithOrderProducts(orderId);
    }

    public OrderResponseDto updateOrderStatus(UUID orderId, OrderStatus orderStatus) throws OrderProcException {
        Order order = orderRepository.findByOrderIdAndIsDeletedFalse(orderId).orElseThrow( ()->
                // 주문이 존재하지 않습니다.
                new OrderProcException(ApiResultError.ORDER_NO_EXIST)
        );

        Order updateOrder = Order.OrderUpdateStatusBuilder()
                .order(order)
                .orderStatus(orderStatus)
                .isDeleted(false)
                .build();

        return OrderResponseDto.of(orderRepository.save(updateOrder));
    }

    public Page<OrderResponseDto> searchOrders(OrderSearchCriteria orderSearchCriteria, Pageable pageable){
        return orderRepository.searchOrders(orderSearchCriteria, pageable);
    }

}
