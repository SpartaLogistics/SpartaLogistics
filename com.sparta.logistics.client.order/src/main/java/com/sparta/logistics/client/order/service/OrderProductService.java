package com.sparta.logistics.client.order.service;

import com.sparta.logistics.client.order.common.exception.OrderProcException;
import com.sparta.logistics.client.order.dto.*;
import com.sparta.logistics.client.order.model.Order;
import com.sparta.logistics.client.order.model.OrderProduct;
import com.sparta.logistics.client.order.repository.OrderProductRepository;
import com.sparta.logistics.common.type.ApiResultError;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderProductService {

    private final OrderProductRepository orderProductRepository;

    public List<OrderProductResponseDto> findByOrderId(UUID orderId) throws OrderProcException {
        return orderProductRepository.findByOrderId(orderId).stream()
                .map(OrderProductResponseDto::of)
                .toList();
    }

    public OrderProduct create(OrderProduct orderProduct) {
        // TODO product 존재여부, 수량, 상태 확인

        return orderProductRepository.save(orderProduct);
    }

    public OrderProductResponseDto create(UUID orderId, OrderProductRequestDto orderRequestDto) throws OrderProcException {
        OrderProduct orderProduct = OrderProduct.OrderProductCreateBuilder()
                .orderId(orderId)
                .orderProductRequestDto(orderRequestDto)
                .build();
        return OrderProductResponseDto.of(this.create(orderProduct));
    }

    public List<OrderProduct> createOrderProducts(UUID orderId, List<OrderProductRequestDto> orderProducts) {
        List<OrderProduct> newOrderProducts = new ArrayList<>();

        for(OrderProductRequestDto orderProductRequestDto : orderProducts) {
            OrderProduct orderProduct = OrderProduct.OrderProductCreateBuilder()
                            .orderId(orderId)
                            .orderProductRequestDto(orderProductRequestDto)
                            .build();

            newOrderProducts.add(this.create(orderProduct));
        }

        return newOrderProducts;
    }

    // 기존에 저장된 주문 품목을 삭제처리하고 새로 저장
    public void deleteAndCreateOrderProducts(UUID orderId, List<OrderProductRequestDto> orderProducts) throws OrderProcException {
        // 기존 품목 삭제
        this.deleteOrderProducts(orderId);

        // 저장
        this.createOrderProducts(orderId, orderProducts);
    }

    public void deleteOrderProduct(UUID orderProductId) throws OrderProcException {
        OrderProduct orderProduct = orderProductRepository.findByOrderProductId(orderProductId).orElseThrow(()->
                new OrderProcException(ApiResultError.ORDER_PRODUCT_NO_EXIST)
        );

        orderProduct.softDelete();
        orderProductRepository.save(orderProduct);
    }

    // 품목 전체 삭제 처리
    public void deleteOrderProducts(UUID orderId) throws OrderProcException {
        List<OrderProduct> newOrderProducts = orderProductRepository.findByOrderId(orderId);

        for(OrderProduct orderProduct : newOrderProducts) {
            UUID orderProductId = orderProduct.getOrderProductId();
            this.deleteOrderProduct(orderProductId);
        }
    }

    public OrderProduct updateOrder(OrderProductRequestDto orderProductRequestDto) throws OrderProcException {
        UUID orderProductId = orderProductRequestDto.getOrderProductId();
        OrderProduct orderProduct = orderProductRepository.findByOrderProductId(orderProductId)
                .orElseThrow(() -> new OrderProcException(ApiResultError.ORDER_PRODUCT_NO_EXIST));

        // TODO 수량 변경 시 상품의 재고량 확인 필요
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
    public List<OrderProductResponseDto> deleteAndCreateProducts(UUID orderId, List<OrderProductRequestDto> newProducts) throws OrderProcException {
        // 삭제 처리
        List<OrderProduct> orderProducts = orderProductRepository.findByOrderId(orderId);
        for(OrderProduct orderProduct : orderProducts) {
            orderProduct.softDelete();
            orderProductRepository.save(orderProduct);
        }

        // 재저장
        List<OrderProductResponseDto> retList = new ArrayList<>();
        for(OrderProductRequestDto newOrderProduct : newProducts) {
            retList.add(this.create(orderId, newOrderProduct));
        }

        return retList;
    }

}
