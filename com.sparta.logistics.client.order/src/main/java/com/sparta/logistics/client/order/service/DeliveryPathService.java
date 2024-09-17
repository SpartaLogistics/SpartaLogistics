package com.sparta.logistics.client.order.service;

import com.sparta.logistics.client.order.common.exception.OrderProcException;
import com.sparta.logistics.client.order.dto.DeliveryPathRequestDto;
import com.sparta.logistics.client.order.dto.DeliveryPathResponseDto;
import com.sparta.logistics.client.order.model.Delivery;
import com.sparta.logistics.client.order.model.DeliveryPath;
import com.sparta.logistics.client.order.repository.DeliveryPathRepository;
import com.sparta.logistics.common.type.ApiResultError;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryPathService {

    private final DeliveryPathRepository deliveryPathRepository;

    public DeliveryPathResponseDto createDeliveryPath(DeliveryPathRequestDto deliveryPathRequestDto) {
        UUID deliveryId = deliveryPathRequestDto.getDeliveryId();
        Delivery delivery = Delivery.builder()
                .deliveryId(deliveryId)
                .build();

        DeliveryPath deliveryPath = DeliveryPath.DeliveryPathCreateBuilder()
                .deliveryPathRequestDto(deliveryPathRequestDto)
                .delivery(delivery)
                .build();

        return DeliveryPathResponseDto.of(deliveryPathRepository.save(deliveryPath));
    }

    public List<DeliveryPathResponseDto> getAllDeliveryPaths(Delivery delivery) {
        return deliveryPathRepository.findAllByDeliveryAndIsDeletedFalse(delivery).stream()
                .map(DeliveryPathResponseDto::of)
                .toList();
    }

    public DeliveryPathResponseDto getDeliveryPath(UUID deliveryPathId) throws OrderProcException {
        DeliveryPath deliveryPath = deliveryPathRepository.findByDeliveryPathIdAndIsDeletedFalse(deliveryPathId).orElseThrow(()->
                new OrderProcException(ApiResultError.DELIVERY_PATH_NO_EXIST));
        return DeliveryPathResponseDto.of(deliveryPath);
    }

    public DeliveryPathResponseDto updateDeliveryPath(DeliveryPathRequestDto deliveryPathRequestDto) throws OrderProcException {
        UUID deliveryPathId = deliveryPathRequestDto.getDeliveryPathId();
        DeliveryPath deliveryPath = deliveryPathRepository.findByDeliveryPathIdAndIsDeletedFalse(deliveryPathId).orElseThrow(()->
                new OrderProcException(ApiResultError.DELIVERY_PATH_NO_EXIST));

        return DeliveryPathResponseDto.of(deliveryPathRepository.save(deliveryPath));
    }

    public DeliveryPathResponseDto deleteDeliveryPath(UUID deliveryPathId) throws OrderProcException {
        DeliveryPath deliveryPath = deliveryPathRepository.findByDeliveryPathIdAndIsDeletedFalse(deliveryPathId).orElseThrow(()->
                new OrderProcException(ApiResultError.DELIVERY_PATH_NO_EXIST));

        deliveryPath.softDelete();
        return DeliveryPathResponseDto.of(deliveryPathRepository.save(deliveryPath));
    }

    public void deleteAllDeliveryPaths(UUID deliveryId) throws OrderProcException {
        Delivery delivery = Delivery.builder()
                .deliveryId(deliveryId)
                .build();
        List<DeliveryPath> deliveryPathList = deliveryPathRepository.findAllByDeliveryAndIsDeletedFalse(delivery);
        for (DeliveryPath deliveryPath : deliveryPathList) {
            UUID deliveryPathId = deliveryPath.getDeliveryPathId();
            this.deleteDeliveryPath(deliveryPathId);
        }
    }

    public List<DeliveryPathResponseDto> createDeliveryPaths(UUID deliveryId, List<DeliveryPathRequestDto> deliveryPathRequestDtos) throws OrderProcException {
        List<DeliveryPath> newDeliveryPaths = new ArrayList<>();
        Delivery delivery = Delivery.builder()
                .deliveryId(deliveryId)
                .build();
        for(DeliveryPathRequestDto deliveryPathRequestDto : deliveryPathRequestDtos){
            deliveryPathRequestDto.setDeliveryId(deliveryId);
            DeliveryPath deliveryPath = DeliveryPath.DeliveryPathCreateBuilder()
                    .deliveryPathRequestDto(deliveryPathRequestDto)
                    .delivery(delivery)
                    .build();
            newDeliveryPaths.add(deliveryPath);
            log.debug("!!!!!!!!!!!!! {}", deliveryPath);
        }
        return deliveryPathRepository.saveAll(newDeliveryPaths).stream()
                .map(DeliveryPathResponseDto::of)
                .toList();

    }

}
