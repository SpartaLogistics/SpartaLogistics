package com.sparta.logistics.client.user.service;

import com.sparta.logistics.client.user.client.HubClient;
import com.sparta.logistics.client.user.common.exception.UserException;
import com.sparta.logistics.client.user.dto.DeliveryManagerResponseDto;
import com.sparta.logistics.client.user.dto.HubResponseDto;
import com.sparta.logistics.client.user.dto.ManagerRequestDto;
import com.sparta.logistics.client.user.dto.UpdateManagerDto;
import com.sparta.logistics.client.user.model.DeliveryManager;
import com.sparta.logistics.client.user.model.User;
import com.sparta.logistics.client.user.repository.DeliveryManagerRepository;
import com.sparta.logistics.client.user.repository.UserRepository;
import com.sparta.logistics.common.model.ApiResult;
import com.sparta.logistics.common.type.ApiResultError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class DeliveryManagerService {
    private final DeliveryManagerRepository deliveryManagerRepository;
    private final UserRepository userRepository;
    private final HubClient hubClient;

    public DeliveryManagerResponseDto createDeliveryManager(ManagerRequestDto requestDto, Long userid) throws UserException {
        User user = userRepository.findByIdAndIsDeletedFalse(userid)
                        .orElseThrow(()-> new UserException(ApiResultError.USER_NO_EXIST));
        ApiResult apiResult = hubClient.getDeliveryManagers(requestDto.getHub_name());
        HubResponseDto hubResponseDto = apiResult.getResultDataAs(HubResponseDto.class);
        UUID hubId = hubResponseDto.getHubId();
        DeliveryManager deliveryManager = DeliveryManager.createDeliveryManager(hubId, user, requestDto.getSlackId(),requestDto.getDeliveryManagerType());
        return DeliveryManagerResponseDto.of(deliveryManagerRepository.save(deliveryManager));
    }

    public List<DeliveryManagerResponseDto> getAllDeliveryManagers() throws UserException {
        List<DeliveryManager> deliveryManagers = deliveryManagerRepository.findAll();
        return deliveryManagers.stream().map(DeliveryManagerResponseDto::of).toList();
    }

    public DeliveryManagerResponseDto getDeliveryManagerById(UUID deliveryId) throws UserException {
        DeliveryManager deliveryManager = deliveryManagerRepository.findByIdAndIsDeletedFalse(deliveryId)
                .orElseThrow(()-> new UserException(ApiResultError.DELIVERY_MANAGER_NO_EXIST));
        return DeliveryManagerResponseDto.of(deliveryManager);
    }

    public DeliveryManagerResponseDto patchDeliveryManager(UUID deliveryId, UpdateManagerDto requestDto) throws UserException {
        DeliveryManager deliveryManager = deliveryManagerRepository.findByIdAndIsDeletedFalse(deliveryId)
                .orElseThrow(()-> new UserException(ApiResultError.DELIVERY_MANAGER_NO_EXIST));
        deliveryManager.update(requestDto);
        return DeliveryManagerResponseDto.of(deliveryManagerRepository.save(deliveryManager));
    }

    public DeliveryManagerResponseDto deleteDeliveryManager(UUID deliveryId, Long userid) throws UserException {
        DeliveryManager deliveryManager = deliveryManagerRepository.findByIdAndIsDeletedFalse(deliveryId)
                .orElseThrow(()-> new UserException(ApiResultError.DELIVERY_MANAGER_NO_EXIST));
        deliveryManager.delete(String.valueOf(userid));
        deliveryManager.softDelete();
        return DeliveryManagerResponseDto.of(deliveryManager);
    }
}
