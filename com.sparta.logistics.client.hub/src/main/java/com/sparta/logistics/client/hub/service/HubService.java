package com.sparta.logistics.client.hub.service;


import com.sparta.logistics.client.hub.common.exception.HubException;
import com.sparta.logistics.client.hub.dto.HubRequestDto;
import com.sparta.logistics.client.hub.dto.HubResponseDto;
import com.sparta.logistics.client.hub.model.Hub;
import com.sparta.logistics.client.hub.repository.HubRepository;
import com.sparta.logistics.common.client.UserClient;
import com.sparta.logistics.common.model.UserVO;
import com.sparta.logistics.common.type.ApiResultError;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HubService {

    private final HubRepository hubRepository;
    private final UserClient userClient;

    // 허브 생성 메서드
    // 새로운 허브가 생성되면 전체 허브의 변화가 생겼기 때문에 삭제 필요
    @CacheEvict(cacheNames = "hubAllCache", allEntries = true)
    public HubResponseDto createHub(HubRequestDto requestDto) throws HubException {
        // User 정보 확인
        UserVO user;

        user = userClient.findByUsername(requestDto.getManagerUsername());
        if (user == null) {
            throw new HubException(ApiResultError.USER_NO_EXIST);
        }


        // sequence 유일성 검증
        validateUniqueSequence(requestDto.getSequence());

        // DTO를 사용하여 Hub 객체 생성
        Hub hub = Hub.createHubInfoBuilder()
                .hubRequestDto(requestDto)
                .sequence(requestDto.getSequence())
                .managerUsername(user.getUsername())
                .build();

        return HubResponseDto.of(hubRepository.save(hub));
    }

    @Cacheable(cacheNames = "hubAllCache", key = "getMethodName()")
    @Transactional(readOnly = true)
    public List<HubResponseDto> getAllHubs() throws HubException {
        return hubRepository.findAllByIsDeletedFalse().stream()
                .map(HubResponseDto::of)
                .collect(Collectors.toList());
    }

    @Cacheable(cacheNames = "hubCache", key = "#hubId")
    @Transactional(readOnly = true)
    public HubResponseDto getHubById(UUID hubId) throws HubException {
        Hub hub = hubRepository.findByHubId(hubId)
                .orElseThrow(() -> new HubException(ApiResultError.HUB_NO_EXIST));
        return HubResponseDto.of(hub);
    }

    // 허브 업데이트시 해당 캐시 수정
    @CachePut(cacheNames = "hubCache", key = "#p0")
    @CacheEvict(cacheNames = "hubAllCache", allEntries = true)
    @Transactional
    public HubResponseDto updateHub(UUID hubId, HubRequestDto requestDto) throws HubException {
        Hub hub = hubRepository.findByHubId(hubId)
                .orElseThrow(() -> new HubException(ApiResultError.HUB_NO_EXIST));

        // sequence가 변경되었다면 유일성 검증
        if (requestDto.getSequence() != null && !requestDto.getSequence().equals(hub.getSequence())) {
            validateUniqueSequence(requestDto.getSequence());
        }

        hub.update(requestDto);
        return HubResponseDto.of(hubRepository.save(hub));
    }

    @Transactional
    @CacheEvict(cacheNames = {"hubAllCache", "hubCache"}, key = "#hubId")
    public void deleteHub(UUID hubId, String userId) throws HubException {
        Hub hub = hubRepository.findByHubId(hubId)
                .orElseThrow(() -> new HubException(ApiResultError.HUB_NO_EXIST));
        hub.softDelete(userId);
        hubRepository.save(hub);
    }

    public Hub findHubById(UUID hubId) throws HubException {
        return hubRepository.findByHubId(hubId)
                .orElseThrow(() -> new HubException(ApiResultError.HUB_NO_EXIST));
    }

    // 허브 존재 확인 메서드
    public boolean existsHubById(UUID hubId) {
        return hubRepository.existsById(hubId);
    }

    public List<HubResponseDto> searchHubs(String name, String address) throws HubException {

        List<Hub> hubs = hubRepository.searchHubs(name, address);

        if (hubs.isEmpty()) {
            throw new HubException(ApiResultError.SEARCH_NO_RESULT);
        }
        return hubs.stream().map(HubResponseDto::of).collect(Collectors.toList());
    }

    private void validateUniqueSequence(Integer sequence) throws HubException {
        if (hubRepository.existsBySequenceAndIsDeletedFalse(sequence)) {
            throw new HubException(ApiResultError.HUB_SEQUENCE_DUPLICATE);
        }
    }
}
