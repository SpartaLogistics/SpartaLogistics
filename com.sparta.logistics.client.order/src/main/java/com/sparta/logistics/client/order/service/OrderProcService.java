package com.sparta.logistics.client.order.service;

import com.sparta.logistics.client.order.client.HubClient;
import com.sparta.logistics.client.order.client.dto.HubPathResponseDto;
import com.sparta.logistics.client.order.client.dto.HubResponseDto;
import com.sparta.logistics.client.order.client.dto.ProductResponseDto;
import com.sparta.logistics.client.order.common.exception.OrderProcException;
import com.sparta.logistics.client.order.common.type.OrderStatus;
import com.sparta.logistics.client.order.dto.*;
import com.sparta.logistics.common.model.ApiResult;
import com.sparta.logistics.common.type.ApiResultError;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private final HubClient hubClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final DeliveryPathService deliveryPathService;

    /**
     * TODO 주문 생성
     */
    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) throws OrderProcException {
        // 1.주문 생성
        OrderResponseDto order = orderService.createOrder(orderRequestDto);
        UUID orderId = order.getOrderId();

        // 2.주문 상품 생성
        List<OrderProductRequestDto> orderProductList = orderRequestDto.getOrderProducts();
        if(validateProduct(orderProductList)) { // 상품 검증
            orderProductService.createOrderProducts(orderId, orderProductList);
        }
        // 2-1. 상품 수량 업데이트
        for(OrderProductRequestDto orderProductRequestDto : orderProductList) {
            UUID productId = orderProductRequestDto.getProductId();
            int quantity = orderProductRequestDto.getQuantity();
            ApiResult updateProduct = hubClient.externalDecreaseQuantity(productId, quantity);

            // 상품 수량 업데이트 실패 시
            if(updateProduct.getResultCode().equals("0000")) {
                throw new OrderProcException(ApiResultError.ORDER_PRODUCT_SAVE_ERROR);
            }
        }

        // 3.배달 생성
        // 출발지 / 배송지 확인
        DeliveryRequestDto deliveryInfo = orderRequestDto.getDeliveryInfo();
        if(validateDelivery(deliveryInfo)) { // 출발지, 베송지 확인
            deliveryInfo.setOrderId(orderId);
        }
        DeliveryResponseDto delivery = deliveryService.createDelivery(deliveryInfo);

        // 4.배달 경로 생성
        UUID deliveryId = delivery.getDeliveryId();
        UUID arrivalId = deliveryInfo.getArrivalId();
        UUID departureId = deliveryInfo.getDepartureId();

        // hub path 조회 후 저장
        ApiResult retHubPaths = hubClient.getHubPaths(departureId, arrivalId);
        List<HubPathResponseDto> hubPaths = retHubPaths.getResultDataAsList(HubPathResponseDto.class);

        List<DeliveryPathRequestDto> deliveryPaths = hubPathToDeliveryPath(deliveryId, hubPaths);

        deliveryPathService.createDeliveryPaths(deliveryId, deliveryPaths);

        return null;
    }

    /**
     * 주문 직접 취소
     * @param orderId
     * @return
     * @throws OrderProcException
     */
    @Transactional
    public OrderResponseDto deleteOrder(UUID orderId) throws OrderProcException {
        OrderResponseDto order = orderService.getOrder(orderId);

        // 주문 삭제
        orderService.deleteOrder(orderId, OrderStatus.ORDER_CANCELED);

        // 주문 상품 삭제 -> product 수량 원복
        List<OrderProductResponseDto> orderProducts = orderProductService.findByOrderId(orderId);
        for (OrderProductResponseDto orderProductResponseDto : orderProducts) {
            UUID productId = orderProductResponseDto.getProductId();
            int quantity = orderProductResponseDto.getQuantity();

            // TODO increaseQuantity 호출해서 원복 혹은 message 전달
            // 1. increaseQuantity
            ApiResult updateProduct = hubClient.increaseQuantity(productId, quantity);

            if (updateProduct.getResultCode().equals("0000")) {
                throw new OrderProcException(ApiResultError.ORDER_PRODUCT_SAVE_ERROR);
            }

            // 2. kafka로 데이터 원복요청 메시지 전송
            // ProductRequestDto productRequestDto = new ProductRequestDto(); TODO 현재 클래스 생성 x
            //kafkaTemplate.send("product-deleted", EventSerializer.serialize(productRequestDto));
        }
        orderProductService.deleteOrderProducts(orderId);

        // 배달 삭제
        DeliveryResponseDto deliveryResponseDto = deliveryService.deleteDelivery(order.getDeliveryId());

        // 배달 경로 삭제
        UUID deliveryId = deliveryResponseDto.getDeliveryId();
        deliveryPathService.deleteDeliveryPath(deliveryId);

        return order;
    }

    /**
     * 상품 삭제처리로 인한 주문 취소
     * @param orderId
     * @return
     * @throws OrderProcException
     */
    @Transactional
    @KafkaListener(topics = "product-deleted", groupId = "product-service")
    public OrderResponseDto cancelOrderIfProductDeleted(UUID orderId) throws OrderProcException {
        OrderResponseDto order = orderService.getOrder(orderId);

        // 주문 삭제
        orderService.deleteOrder(orderId, OrderStatus.ORDER_AUTO_CANCELED);

        // 주문 상품 삭제 -> product 수량 원복
        List<OrderProductResponseDto> orderProducts = orderProductService.findByOrderId(orderId);
        for (OrderProductResponseDto orderProductResponseDto : orderProducts) {
            UUID productId = orderProductResponseDto.getProductId();
            int quantity = orderProductResponseDto.getQuantity();

            // TODO increaseQuantity 호출해서 원복 혹은 message 전달
            // 1. increaseQuantity
            ApiResult updateProduct = hubClient.increaseQuantity(productId, quantity);

            if (updateProduct.getResultCode().equals("0000")) {
                throw new OrderProcException(ApiResultError.ORDER_PRODUCT_SAVE_ERROR);
            }

            // 2. kafka로 데이터 원복요청 메시지 전송
            // ProductRequestDto productRequestDto = new ProductRequestDto(); TODO 현재 클래스 생성 x
            //kafkaTemplate.send("product-deleted", EventSerializer.serialize(productRequestDto));
        }
        orderProductService.deleteOrderProducts(orderId);

        // 배달 삭제
        DeliveryResponseDto deliveryResponseDto = deliveryService.deleteDelivery(order.getDeliveryId());

        // 배달 경로 삭제
        UUID deliveryId = deliveryResponseDto.getDeliveryId();
        deliveryPathService.deleteDeliveryPath(deliveryId);

        return order;
    }

    // TODO 분리
    private boolean validateProduct(List<OrderProductRequestDto> orderProductList) throws OrderProcException {
        boolean isValid = false;
        for(OrderProductRequestDto orderProductRequestDto : orderProductList) {
            UUID productId = orderProductRequestDto.getProductId();
            int reqQuantity = orderProductRequestDto.getQuantity();

            ApiResult retProduct = hubClient.getProduct(productId);
            ProductResponseDto product = retProduct.getResultDataAs(ProductResponseDto.class);

            if(null == product) {
                throw new OrderProcException(ApiResultError.ORDER_PRODUCT_NO_EXIST);
            }

            if(product.isDeleted()) {
                throw new OrderProcException(ApiResultError.ORDER_PRODUCT_NOT_AVAILABLE);
            }

            if(product.getQuantity() > reqQuantity) {
                throw new OrderProcException(ApiResultError.ORDER_PRODUCT_INSUFFICIENT);
            }
        }
        isValid = true;

        return isValid;
    }

    private boolean validateDelivery(DeliveryRequestDto deliveryInfo) throws OrderProcException {
        boolean isValid = false;

        // 도착지 허브
        UUID departureId = deliveryInfo.getDepartureId();
        ApiResult retDepartureHub = hubClient.getHubById(departureId);
        HubResponseDto departureHub = retDepartureHub.getResultDataAs(HubResponseDto.class);

        if(null == departureHub) {
            throw new OrderProcException(ApiResultError.DELIVERY_NOT_EXIST_DEPARTURE_HUB);
        }

        if(departureHub.is_deleted()) {
            throw new OrderProcException(ApiResultError.DELIVERY_NOT_AVAILABLE_DEPARTURE_HUB);
        }

        // 출발지 허브
        UUID arrivalId = deliveryInfo.getArrivalId();
        ApiResult retArrivalHub = hubClient.getHubById(arrivalId);
        HubResponseDto arrivalHub = retDepartureHub.getResultDataAs(HubResponseDto.class);

        if(null == arrivalHub) {
            throw new OrderProcException(ApiResultError.DELIVERY_NOT_EXIST_ARRIVAL_HUB);
        }

        if(departureHub.is_deleted()) {
            throw new OrderProcException(ApiResultError.DELIVERY_NOT_AVAILABLE_ARRIVAL_HUB);
        }

        isValid = true;

        return isValid;

    }


    public boolean validateHubPaths(List<HubPathResponseDto> hubPaths) throws OrderProcException {
        boolean isValid = false;

        for(HubPathResponseDto hubPath : hubPaths) {


        }
        return isValid;
    }

    private List<DeliveryPathRequestDto> hubPathToDeliveryPath(UUID deliveryId, List<HubPathResponseDto> hubPaths) throws OrderProcException {
        List<DeliveryPathRequestDto> deliveryPaths = new ArrayList<>();
        for(HubPathResponseDto hubPath : hubPaths) {
            DeliveryPathRequestDto deliveryPathRequestDto = new DeliveryPathRequestDto();
            deliveryPathRequestDto.setDeliveryId(deliveryId);
            deliveryPathRequestDto.setArrivalId(hubPath.getHubId());
            deliveryPathRequestDto.setDeliveryPathId(hubPath.getNextHubId());
            deliveryPathRequestDto.setExpectedTime(hubPath.getDuration());
            deliveryPaths.add(deliveryPathRequestDto);
        }
        return deliveryPaths;
    }


    // 주문 수정
    // -> 주문상품 수정 시 기존 상품 delete 처리 후 새로 저장
    // -> 배달 수정 시 기존 경로 delete 처리 후 새로 저장

    public OrderResponseDto updateOrder(OrderRequestDto orderRequestDto) throws OrderProcException {
        UUID orderId = orderRequestDto.getOrderId();

        // 주문건 확인
        OrderResponseDto order = orderService.getOrder(orderId);

        // 주문 품목 재저장
        List<OrderProductRequestDto> orderProductList = orderRequestDto.getOrderProducts();
        if(orderProductList != null) {
            orderProductService.deleteAndCreateProducts(orderId, orderProductList);
        }

        // 배달 정보 저장
        DeliveryRequestDto deliveryInfo = orderRequestDto.getDeliveryInfo();



        return null;
    }
}
