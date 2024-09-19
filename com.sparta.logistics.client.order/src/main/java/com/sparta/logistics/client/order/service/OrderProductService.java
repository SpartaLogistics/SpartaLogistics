package com.sparta.logistics.client.order.service;

import com.sparta.logistics.client.order.common.exception.OrderProcException;
import com.sparta.logistics.client.order.dto.*;
import com.sparta.logistics.client.order.model.OrderProduct;
import com.sparta.logistics.client.order.repository.OrderProductRepository;
import com.sparta.logistics.common.type.ApiResultError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderProductService {

    private final OrderProductRepository orderProductRepository;

    @Transactional(rollbackFor = Exception.class)
    public List<OrderProductResponseDto> findByOrderId(UUID orderId) throws OrderProcException {
        return orderProductRepository.findByOrderId(orderId).stream()
                .map(OrderProductResponseDto::of)
                .toList();
    }

    public OrderProduct create(OrderProduct orderProduct) {

        return orderProductRepository.save(orderProduct);
    }

    public OrderProductResponseDto create(UUID orderId, OrderProductRequestDto orderRequestDto) throws OrderProcException {
        OrderProduct orderProduct = OrderProduct.OrderProductCreateBuilder()
                .orderId(orderId)
                .orderProductRequestDto(orderRequestDto)
                .build();
        return OrderProductResponseDto.of(this.create(orderProduct));
    }

    public List<OrderProductResponseDto> createOrderProducts(UUID orderId, List<OrderProductRequestDto> orderProducts) {
        List<OrderProduct> orderProductList = new ArrayList<>();
        for(OrderProductRequestDto orderProductRequestDto : orderProducts) {
            OrderProduct orderProduct = OrderProduct.OrderProductCreateBuilder()
                            .orderId(orderId)
                            .orderProductRequestDto(orderProductRequestDto)
                            .build();
            orderProductList.add(orderProduct);
        }

        return orderProductRepository.saveAll(orderProductList).stream()
                .map(OrderProductResponseDto::of)
                .toList();
    }

    // 기존에 저장된 주문 품목을 삭제처리하고 새로 저장
    public void deleteAndCreateOrderProducts(UUID orderId, List<OrderProductRequestDto> orderProducts, String userId) throws OrderProcException {
        // 기존 품목 삭제
        this.deleteOrderProducts(orderId, userId);

        // 저장
        this.createOrderProducts(orderId, orderProducts);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteOrderProduct(UUID orderProductId, String userId) throws OrderProcException {
        OrderProduct orderProduct = orderProductRepository.findByOrderProductId(orderProductId).orElseThrow(()->
                new OrderProcException(ApiResultError.ORDER_PRODUCT_NO_EXIST)
        );

        orderProduct.softDelete(userId);
        orderProductRepository.save(orderProduct);
    }

    // 품목 전체 삭제 처리
    @Transactional(rollbackFor = Exception.class)
    public void deleteOrderProducts(UUID orderId, String userId) throws OrderProcException {
        List<OrderProduct> newOrderProducts = orderProductRepository.findByOrderId(orderId);

        for(OrderProduct orderProduct : newOrderProducts) {
            UUID orderProductId = orderProduct.getOrderProductId();
            this.deleteOrderProduct(orderProductId, userId);
        }
    }

    public OrderProduct updateOrder(OrderProductRequestDto orderProductRequestDto) throws OrderProcException {
        UUID orderProductId = orderProductRequestDto.getOrderProductId();
        OrderProduct orderProduct = orderProductRepository.findByOrderProductId(orderProductId)
                .orElseThrow(() -> new OrderProcException(ApiResultError.ORDER_PRODUCT_NO_EXIST));

        int totalQuantity = 100;
        if(totalQuantity < orderProductRequestDto.getQuantity()) {
            // 재고 부족
            throw new OrderProcException(ApiResultError.ORDER_PRODUCT_INSUFFICIENT);
        }

        OrderProduct newOrderProduct = OrderProduct.OrderProductUpdateBuilder()
                .orderProductRequestDto(orderProductRequestDto)
                .build();

        return orderProductRepository.save(newOrderProduct);
    }

    // 삭제 후 재저장
    public List<OrderProductResponseDto> deleteAndCreateProducts(UUID orderId, List<OrderProductRequestDto> newProducts, String userId) throws OrderProcException {
        // 삭제 처리
        List<OrderProduct> orderProducts = orderProductRepository.findByOrderId(orderId);
        for(OrderProduct orderProduct : orderProducts) {
            orderProduct.softDelete(userId);
            orderProductRepository.save(orderProduct);
        }

        // 재저장
        List<OrderProductResponseDto> retList = new ArrayList<>();
        for(OrderProductRequestDto newOrderProduct : newProducts) {
            retList.add(this.create(orderId, newOrderProduct));
        }
        log.info("!!!!!!!!!! ===========> product delete {}", retList);
        return retList;
    }

    // productId로 찾기
    @Transactional(rollbackFor = Exception.class)
    public List<UUID> findDistinctOrderIdsByProductId(UUID productId) {
        return orderProductRepository.findDistinctOrderIdsByProductId(productId);
    }
}
