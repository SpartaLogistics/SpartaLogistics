package com.sparta.logistics.client.hub.service;


import com.sparta.logistics.client.hub.dto.HubRequestDto;
import com.sparta.logistics.client.hub.dto.HubResponseDto;
import com.sparta.logistics.client.hub.model.Hub;
import com.sparta.logistics.client.hub.repository.HubRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HubService {

    private final HubRepository hubRepository;

    // 허브 생성 메서드
    // 새로운 허브가 생성되면 전체 허브의 변화가 생겼기 때문에 삭제 필요
    @CacheEvict(cacheNames = "hubAllCache", allEntries = true)
    public HubResponseDto createHub(HubRequestDto requestDto) {
        // DTO를 사용하여 Hub 객체 생성
        Hub hub = Hub.createHubInfoBuilder()
                .hubRequestDto(requestDto)
                .build();

        return HubResponseDto.of(hubRepository.save(hub));
    }

    @Cacheable(cacheNames = "hubAllCache", key = "#pageable")
    public Page<HubResponseDto> getAllHubs(Pageable pageable) {
        return hubRepository.findAll(pageable).map(HubResponseDto::of);
    }

    @Cacheable(cacheNames = "hubCache", key = "#hubId")
    public HubResponseDto getHubById(UUID hubId) {
        Hub hub = hubRepository.findById(hubId)
                .orElseThrow(() -> new EntityNotFoundException("허브를 찾을 수 없습니다. ID: " + hubId));
        return HubResponseDto.of(hub);
    }

    // 허브 업데이트시 해당 캐시 수정
    @CachePut(cacheNames = "hubCache", key = "#p0")
    @CacheEvict(cacheNames = "hubAllCache", allEntries = true)
    @Transactional
    public HubResponseDto updateHub(UUID hubID, HubRequestDto requestDto) {
        Hub hub = hubRepository.findById(hubID)
                .orElseThrow(() -> new EntityNotFoundException("허브를 찾을 수 없습니다."));

        hub.update(requestDto);
        return HubResponseDto.of(hubRepository.save(hub));
    }

    @Transactional
    @CacheEvict(cacheNames = {"hubAllCache", "hubCache"}, key = "#hubId")
    public void deleteHub(UUID hubId) {
        Hub hub = hubRepository.findById(hubId)
                .orElseThrow(() -> new EntityNotFoundException("허브를 찾을 수 없습니다. ID: " + hubId));
        hub.softDelete();
        hubRepository.save(hub);
    }

    //TODO : is_deleted false인것만 조회
    public Hub findHubById(UUID hubId) {
        return hubRepository.findById(hubId).orElseThrow(NoSuchElementException::new);
    }
}
