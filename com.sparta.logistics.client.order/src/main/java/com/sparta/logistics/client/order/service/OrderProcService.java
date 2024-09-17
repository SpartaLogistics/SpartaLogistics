package com.sparta.logistics.client.order.service;

import com.sparta.logistics.client.order.client.HubClient;
import com.sparta.logistics.client.order.client.dto.HubPathResponseDto;
import com.sparta.logistics.client.order.client.dto.HubResponseDto;
import com.sparta.logistics.client.order.client.dto.ProductResponseDto;
import com.sparta.logistics.client.order.common.exception.OrderProcException;
import com.sparta.logistics.client.order.common.type.OrderStatus;
import com.sparta.logistics.client.order.dto.*;
import com.sparta.logistics.common.kafka.ProductDeleted;
import com.sparta.logistics.common.model.ApiResult;
import com.sparta.logistics.common.model.EventSerializer;
import com.sparta.logistics.common.type.ApiResultError;
import org.aspectj.weaver.ast.Or;
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
     * 주문 생성
     * @param orderRequestDto
     * @return
     * @throws OrderProcException
     */
    @Transactional(rollbackFor = Exception.class)
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) throws OrderProcException {
        // 1.주문 생성
        OrderResponseDto order = orderService.createOrder(orderRequestDto);
        UUID orderId = order.getOrderId();

        // 2.주문 상품 생성
        List<OrderProductRequestDto> orderProductList = orderRequestDto.getOrderProducts();
        if(validateProduct(orderProductList)) { // 상품 검증
            List<OrderProductResponseDto> orderProducts = orderProductService.createOrderProducts(orderId, orderProductList);
            order.setProducts(orderProducts);
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
        order.setDelivery(delivery);

        // 4.배달 경로 생성
        UUID deliveryId = delivery.getDeliveryId();
        UUID arrivalId = deliveryInfo.getArrivalId();
        UUID departureId = deliveryInfo.getDepartureId();

        // hub path 조회 후 저장
        ApiResult retHubPaths = hubClient.getHubPaths(departureId, arrivalId);
        List<HubPathResponseDto> hubPaths = retHubPaths.getResultDataAsList(HubPathResponseDto.class);

        List<DeliveryPathRequestDto> deliveryPaths = hubPathToDeliveryPath(deliveryId, hubPaths);

        List<DeliveryPathResponseDto> deliveryPathResponseDto = deliveryPathService.createDeliveryPaths(deliveryId, deliveryPaths);
        order.setDeliveryPaths(deliveryPathResponseDto);

        return order;
    }

    /**
     * 주문 직접 취소
     * @param orderId
     * @return
     * @throws OrderProcException
     */
    @Transactional(rollbackFor = Exception.class)
    public OrderResponseDto deleteOrder(UUID orderId, OrderStatus orderStatus) throws OrderProcException {
        OrderResponseDto order = orderService.getOrder(orderId);

        // 주문 삭제
        orderService.deleteOrder(orderId, orderStatus);

        // 주문 상품 삭제 -> product 수량 원복
        List<OrderProductResponseDto> orderProducts = orderProductService.findByOrderId(orderId);
        for (OrderProductResponseDto orderProductResponseDto : orderProducts) {
            UUID productId = orderProductResponseDto.getProductId();
            int quantity = orderProductResponseDto.getQuantity();

            ApiResult updateProduct = hubClient.increaseQuantity(productId, quantity);
            if (updateProduct.getResultCode().equals("0000")) {
                throw new OrderProcException(ApiResultError.ORDER_PRODUCT_SAVE_ERROR);
            }

        }
        orderProductService.deleteOrderProducts(orderId);

        // 배달 삭제
        DeliveryResponseDto deliveryResponseDto = deliveryService.deleteDeliveryByOrderId(orderId);

        // 배달 경로 삭제
        UUID deliveryId = deliveryResponseDto.getDeliveryId();
        deliveryPathService.deleteAllDeliveryPaths(deliveryId);

        return order;
    }

    /**
     * 상품 삭제처리로 인한 주문 취소
     * @param message
     * @throws OrderProcException
     */
    @Transactional(rollbackFor = Exception.class)
    @KafkaListener(topics = "product-deleted", groupId = "product-service")
    public void cancelOrderIfProductDeleted(String message) throws OrderProcException {
        log.debug("--------------->> 상품 삭제로 인한 주문 삭제");
        ProductDeleted event = EventSerializer.deserialize(message, ProductDeleted.class);

        UUID productId = event.getProductId();
        List<UUID> deleteOrderList = orderProductService.findDistinctOrderIdsByProductId(productId);

        for(UUID orderId : deleteOrderList) {
            this.deleteOrder(orderId, OrderStatus.ORDER_AUTO_CANCELED);
        }

    }

    /**
     * 주문 수정
     * -> 주문상품 수정 시 기존 상품 delete 처리 후 새로 저장
     * -> 배달 수정 시 기존 경로 delete 처리 후 새로 저장
     * @param orderRequestDto
     * @return
     * @throws OrderProcException
     */
    @Transactional(rollbackFor = Exception.class)
    public OrderResponseDto updateOrder(OrderRequestDto orderRequestDto) throws OrderProcException {
        UUID orderId = orderRequestDto.getOrderId();

        // 주문건 확인
        OrderResponseDto order = orderService.getOrder(orderId);

        OrderResponseDto retOrder = orderService.updateOrder(orderRequestDto);

        // 주문 품목 재저장
        List<OrderProductRequestDto> orderProductList = orderRequestDto.getOrderProducts();
        if(orderProductList != null) {
            orderProductService.deleteAndCreateProducts(orderId, orderProductList);
        }

        // 배달 정보 저장
        DeliveryRequestDto deliveryInfo = orderRequestDto.getDeliveryInfo();
        UUID deliveryId = deliveryInfo.getDeliveryId();
        UUID arrivalId = deliveryInfo.getArrivalId();
        UUID departureId = deliveryInfo.getDepartureId();

        DeliveryResponseDto delivery = deliveryService.getDeliveryById(deliveryId);

        if(arrivalId != delivery.getArrivalID() || departureId != delivery.getDepartureId()) {
            deliveryPathService.deleteAllDeliveryPaths(deliveryId);

            // 새 경로 저장
            ApiResult retHubPaths = hubClient.getHubPaths(departureId, arrivalId);
            List<HubPathResponseDto> hubPaths = retHubPaths.getResultDataAsList(HubPathResponseDto.class);

            List<DeliveryPathRequestDto> deliveryPaths = hubPathToDeliveryPath(deliveryId, hubPaths);

            deliveryPathService.createDeliveryPaths(deliveryId, deliveryPaths);
        }

        return retOrder;
    }

    private boolean validateProduct(List<OrderProductRequestDto> orderProductList) throws OrderProcException {
        boolean isValid = false;
        for(OrderProductRequestDto orderProductRequestDto : orderProductList) {
            UUID productId = orderProductRequestDto.getProductId();
            int reqQuantity = orderProductRequestDto.getQuantity();

            ApiResult retProduct = hubClient.getProduct(productId);
            ProductResponseDto product = retProduct.getResultDataAs(ProductResponseDto.class);
            log.debug("----------------> product {}", product);
            if(null == product) {
                throw new OrderProcException(ApiResultError.ORDER_PRODUCT_NO_EXIST);
            }

            if(product.isDeleted()) {
                throw new OrderProcException(ApiResultError.ORDER_PRODUCT_NOT_AVAILABLE);
            }

            if(product.getQuantity() == 0 || product.getQuantity() < reqQuantity) {
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
        HubResponseDto arrivalHub = retArrivalHub.getResultDataAs(HubResponseDto.class);

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
            UUID hubId = hubPath.getHubId();
            ApiResult retHub = hubClient.getHubById(hubId);
            HubResponseDto hubResponseDto = retHub.getResultDataAs(HubResponseDto.class);

            UUID nextHubId = hubPath.getNextHubId();
            ApiResult retNexHub = hubClient.getHubById(nextHubId);
            HubResponseDto nexHubResponseDto = retNexHub.getResultDataAs(HubResponseDto.class);

            if(null == hubResponseDto) {
                throw new OrderProcException(ApiResultError.DELIVERY_NOT_EXIST_DEPARTURE_HUB);
            }

            if(null == nexHubResponseDto) {
                throw new OrderProcException(ApiResultError.DELIVERY_NOT_EXIST_ARRIVAL_HUB);
            }

        }
        isValid = true;
        return isValid;
    }

    private List<DeliveryPathRequestDto> hubPathToDeliveryPath(UUID deliveryId, List<HubPathResponseDto> hubPaths) throws OrderProcException {
        List<DeliveryPathRequestDto> deliveryPaths = new ArrayList<>();
        int sequence = 0;
        for(HubPathResponseDto hubPath : hubPaths) {
            sequence++;
            DeliveryPathRequestDto deliveryPathRequestDto = new DeliveryPathRequestDto();
            deliveryPathRequestDto.setDeliveryId(deliveryId);
            deliveryPathRequestDto.setArrivalId(hubPath.getNextHubId());    // 도착 hub
            deliveryPathRequestDto.setDepartureId(hubPath.getHubId());      // 출발 hub
            deliveryPathRequestDto.setDeliveryPathId(hubPath.getNextHubId());
            deliveryPathRequestDto.setExpectedTime(hubPath.getDuration());
            deliveryPathRequestDto.setSequence(sequence);

            deliveryPaths.add(deliveryPathRequestDto);
        }
        return deliveryPaths;
    }

}
