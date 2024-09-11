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
}
