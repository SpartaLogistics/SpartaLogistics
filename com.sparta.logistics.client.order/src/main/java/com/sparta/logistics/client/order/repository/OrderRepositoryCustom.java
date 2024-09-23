package com.sparta.logistics.client.order.repository;

import com.sparta.logistics.client.order.common.exception.OrderProcException;
import com.sparta.logistics.client.order.dto.OrderRequestDto;
import com.sparta.logistics.client.order.dto.OrderResponseDto;
import com.sparta.logistics.client.order.dto.OrderSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderRepositoryCustom {

    OrderResponseDto findByOrderIdWithOrderProducts(UUID orderId) throws OrderProcException;

    // Order List
    Page<OrderResponseDto> searchOrders(OrderSearchCriteria orderSearchCriteria, Pageable pageable);
}
