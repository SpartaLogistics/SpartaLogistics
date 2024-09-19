package com.sparta.logistics.client.order.service;

import com.sparta.logistics.client.order.common.exception.OrderProcException;
import com.sparta.logistics.client.order.dto.DeliveryRequestDto;
import com.sparta.logistics.client.order.dto.DeliveryResponseDto;
import com.sparta.logistics.client.order.model.Delivery;
import com.sparta.logistics.client.order.model.Order;
import com.sparta.logistics.client.order.repository.DeliveryRepository;
import com.sparta.logistics.common.type.ApiResultError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    public DeliveryResponseDto createDelivery(DeliveryRequestDto deliveryRequestDto) {
        Delivery delivery = Delivery.DeliveryCreateBuilder()
                .deliveryRequestDto(deliveryRequestDto)
                .build();

        return DeliveryResponseDto.of(deliveryRepository.save(delivery));
    }

    public List<DeliveryResponseDto> getAllDeliveries() {
        return deliveryRepository.findAllByIsDeletedFalse().stream()
                .map(DeliveryResponseDto::of)
                .toList();

    }

    public DeliveryResponseDto getDeliveryById(UUID deliveryId) throws OrderProcException {
        return DeliveryResponseDto.of(deliveryRepository.findById(deliveryId).orElseThrow(()->
                new OrderProcException(ApiResultError.DELIVERY_NO_EXIST)));
    }

    public DeliveryResponseDto updateDelivery(DeliveryRequestDto deliveryRequestDto) throws OrderProcException {
        UUID deliveryId = deliveryRequestDto.getDeliveryId();
        Delivery delivery = deliveryRepository.findByDeliveryIdAndIsDeletedFalse(deliveryId).orElseThrow(()->
                new OrderProcException(ApiResultError.DELIVERY_NO_EXIST)
        );

        // TODO 수정: 마스터 관리자, 해당 허브 관리자, 그리고 해당 배송 담당자만

        return DeliveryResponseDto.of(deliveryRepository.save(delivery));
    }

    @Transactional(rollbackFor = Exception.class)
    public DeliveryResponseDto deleteDeliveryByOrderId(UUID orderId, String userId) throws OrderProcException {

        Delivery delivery = deliveryRepository.findByOrderId(orderId);
        log.info("----------------> !!!!!!!  delete {}", delivery);
        delivery.softDelete(userId);
        return DeliveryResponseDto.of(deliveryRepository.save(delivery));
    }


}
