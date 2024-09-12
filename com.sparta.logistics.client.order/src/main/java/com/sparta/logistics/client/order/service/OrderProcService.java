package com.sparta.logistics.client.order.service;

import com.sparta.logistics.client.order.common.exception.OrderProcException;
import com.sparta.logistics.client.order.dto.*;
import com.sparta.logistics.client.order.model.Order;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrderProcService {

    private final OrderService orderService;
    private final OrderProductService orderProductService;
    private final DeliveryService deliveryService;

    /**
     * TODO 주문 생성
     */
    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) throws OrderProcException {
        // 1.주문 생성
        // 출발지 / 배송지 확인
        OrderResponseDto order = orderService.createOrder(orderRequestDto);

        // 2.주문 상품 생성
        // 상품 확인 -> null / isDeleted
        UUID orderId = order.getOrderId();
        List<OrderProductRequestDto> orderProductList = orderRequestDto.getOrderProducts();
        orderProductService.createOrderProducts(orderId, orderProductList);

        // 3.배달 생성
        DeliveryRequestDto deliveryInfo = orderRequestDto.getDeliveryInfo();
        deliveryInfo.setOrderId(orderId);
        // hub 확인
        DeliveryResponseDto delivery = deliveryService.createDelivery(deliveryInfo);

        // 4.배달 경로 생성
        UUID deliveryId = delivery.getDeliveryId();
        // hub path 조회 후 저장

        return null;
    }

    // 주문 수정
    // -> 주문상품 수정 시 기존 상품 delete 처리 후 새로 저장
    // -> 배달 수정 시 기존 경로 delete 처리 후 새로 저장

    // 주문 삭제
    // 주문 상품 삭제 -> product 수량 원복Ø
    // 배달 삭제
    // 배달 경로 삭제

}
