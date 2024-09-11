package com.sparta.logistics.client.order.service;

import com.sparta.logistics.client.order.dto.OrderRequestDto;
import com.sparta.logistics.client.order.dto.OrderResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrderProcService {

    private final OrderService orderService;
    private final OrderProductService orderProductService;

    /**
     * TODO 주문 생성
     */
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        // 1.주문 생성
        // 출발지 / 배송지 확인

        // 2.주문 상품 생성
        // 상품 확인 -> null / isDeleted

        // 3.배달 생성

        // 4.배달 경로 생성

        return null;
    }

    // 주문 수정
    // -> 주문상품 수정 시 기존 상품 delete 처리 후 새로 저장
    // -> 배달 수정 시 기존 경로 delete 처리 후 새로 저장

    // 주문 삭제
    // 주문 상품 삭제 -> product 수량 원복
    // 배달 삭제
    // 배달 경로 삭제

}
