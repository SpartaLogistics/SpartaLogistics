package com.sparta.logistics.client.user.service;

import com.sparta.logistics.client.user.common.exception.UserException;
import com.sparta.logistics.client.user.dto.DeliveryManagerResponse;
import com.sparta.logistics.client.user.dto.ManagerRequestDto;
import com.sparta.logistics.client.user.model.DeliveryManager;
import com.sparta.logistics.client.user.model.User;
import com.sparta.logistics.client.user.repository.DeliveryManagerRepository;
import com.sparta.logistics.client.user.repository.UserRepository;
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

    public DeliveryManagerResponse createDeliveryManager(ManagerRequestDto requestDto, Long userid) throws UserException {
        User user = userRepository.findById(userid)
                        .orElseThrow(()-> new UserException(ApiResultError.USER_NO_EXIST));
        DeliveryManager deliveryManager = DeliveryManager.createDeliveryManager(user, requestDto.getSlackId(),requestDto.getDeliveryManagerType());
        return DeliveryManagerResponse.of(deliveryManagerRepository.save(deliveryManager));
    }

    public List<DeliveryManagerResponse> getAllDeliveryManagers() throws UserException {
        List<DeliveryManager> deliveryManagers = deliveryManagerRepository.findAll();
        return deliveryManagers.stream().map(DeliveryManagerResponse::of).toList();
    }

    public DeliveryManagerResponse getDeliveryManagerById(UUID deliveryId) throws UserException {
        DeliveryManager deliveryManager = deliveryManagerRepository.findById(deliveryId)
                .orElseThrow(()-> new UserException(ApiResultError.DELIVERY_MANAGER_NO_EXIST));
        return DeliveryManagerResponse.of(deliveryManager);
    }

    public DeliveryManagerResponse patchDeliveryManager(UUID deliveryId, ManagerRequestDto requestDto) throws UserException {
        DeliveryManager deliveryManager = deliveryManagerRepository.findById(deliveryId)
                .orElseThrow(()-> new UserException(ApiResultError.DELIVERY_MANAGER_NO_EXIST));
        deliveryManager.update(requestDto);
        return DeliveryManagerResponse.of(deliveryManagerRepository.save(deliveryManager));
    }

    public DeliveryManagerResponse deleteDeliveryManager(UUID deliveryId, Long userid) throws UserException {
        DeliveryManager deliveryManager = deliveryManagerRepository.findById(deliveryId)
                .orElseThrow(()-> new UserException(ApiResultError.DELIVERY_MANAGER_NO_EXIST));
        deliveryManager.softDelete();
        return DeliveryManagerResponse.of(deliveryManager);
    }
}
