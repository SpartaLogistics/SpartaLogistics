package com.sparta.logistics.client.hub.service;


import com.sparta.logistics.client.hub.dto.HubRequestDto;
import com.sparta.logistics.client.hub.dto.HubResponseDto;
import com.sparta.logistics.client.hub.model.Hub;
import com.sparta.logistics.client.hub.repository.HubRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HubService {

    private final HubRepository hubRepository;

    // 허브 생성 메서드
    public HubResponseDto createHub(HubRequestDto requestDto) {
        // DTO를 사용하여 Hub 객체 생성
        Hub hub = Hub.createHubInfoBuilder()
                .hubRequestDto(requestDto)
                .build();

        return HubResponseDto.of(hubRepository.save(hub));
    }

    public Page<HubResponseDto> getAllHubs(Pageable pageable) {
        return hubRepository.findAll(pageable).map(HubResponseDto::of);
    }

    public ResponseEntity<HubResponseDto> getHubById(UUID hubId) {
        Hub hub = hubRepository.findById(hubId)
                .orElseThrow(() -> new EntityNotFoundException("허브를 찾을 수 없습니다. ID: " + hubId));
        return ResponseEntity.ok(HubResponseDto.of(hub));
    }

    public HubResponseDto updateHub(UUID hubID, HubRequestDto requestDto) {
        Hub hub = hubRepository.findById(hubID)
                .orElseThrow(() -> new EntityNotFoundException("허브를 찾을 수 없습니다."));

        hub.update(requestDto);
        return HubResponseDto.of(hubRepository.save(hub));
    }

    @Transactional
    public void deleteHub(UUID hubId) {
        Hub hub = hubRepository.findById(hubId)
                .orElseThrow(() -> new EntityNotFoundException("허브를 찾을 수 없습니다. ID: " + hubId));
        hub.softDelete();
        hubRepository.save(hub);
    }
}
