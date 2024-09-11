package com.sparta.logistics.client.order.service;

import com.sparta.logistics.client.order.common.exception.OrderException;
import com.sparta.logistics.client.order.dto.OrderRequestDto;
import com.sparta.logistics.client.order.dto.OrderResponseDto;
import com.sparta.logistics.client.order.dto.OrderSearchDto;
import com.sparta.logistics.client.order.model.Order;
import com.sparta.logistics.client.order.repository.OrderRepository;
import com.sparta.logistics.common.type.ApiResultError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    // 주문 생성
    public Order createOrder(OrderRequestDto orderRequestDto) throws OrderException {
        // TODO 데이터 검증
        // 1. 발송처 확인
        // 2. 배송처 확인

        Order order = Order.OrderCreateBuilder()
                .orderDto(orderRequestDto)
                .build();
        return orderRepository.save(order);
    }

    // 주문 목록 조회
    public Page<Order> getOrderList(OrderSearchDto orderSearchDto, Pageable pageable)  {
        // TODO 관리자 외 자신의 주문만 조회 가능
        return orderRepository.findAllByIsDeletedFalse(pageable);
    }

    // 주문 상세 조회
    public Order getOrder(UUID orderId) throws OrderException {
        // 주문 확인
        Order order = orderRepository.findByOrderIdAndIsDeletedFalse(orderId).orElseThrow( ()->
                // 주문이 존재하지 않습니다.
                new OrderException(ApiResultError.ORDER_NO_EXIST)
        );

        // 관리자 외 자신의 주문만 조회 가능
        return order;
    }

    // 주문 삭제
    public Order deleteOrder(UUID orderId) throws OrderException {
        // 주문 확인
        Order order = orderRepository.findByOrderIdAndIsDeletedFalse(orderId).orElseThrow( ()->
            // 주문이 존재하지 않습니다.
            new OrderException(ApiResultError.ORDER_NO_EXIST)
        );
        order.softDelete();
        return orderRepository.save(order);
    }

    public OrderResponseDto updateOrder(OrderRequestDto orderRequestDto) throws OrderException {
        UUID orderId = orderRequestDto.getOrderId();
        // 주문 확인
        Order order = orderRepository.findByOrderIdAndIsDeletedFalse(orderId).orElseThrow( ()->
                // 주문이 존재하지 않습니다.
                new OrderException(ApiResultError.ORDER_NO_EXIST)
        );

        Order updatedOrder = Order.OrderUpdateBuilder()
                .orderDto(orderRequestDto)
                .order(order)
                .build();
        return new OrderResponseDto(orderRepository.save(updatedOrder));


    }

}
