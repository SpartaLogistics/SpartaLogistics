package com.sparta.logistics.client.hub.service;

import com.sparta.logistics.client.hub.common.exception.HubException;
import com.sparta.logistics.client.hub.dto.HubPathRequestDto;
import com.sparta.logistics.client.hub.dto.HubPathResponseDto;
import com.sparta.logistics.client.hub.model.Hub;
import com.sparta.logistics.client.hub.model.HubPath;
import com.sparta.logistics.client.hub.repository.HubPathRepository;
import com.sparta.logistics.common.type.ApiResultError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class HubPathService {


    private final HubService hubService;
    private final HubPathRepository hubPathRepository;

    public HubPathResponseDto createHubPath(HubPathRequestDto requestDto) throws HubException {
        Hub departureHub = hubService.findHubById(requestDto.getDepartureHubId());
        Hub arrivalHub = hubService.findHubById(requestDto.getArrivalHubId());

        HubPath hubPath = HubPath.createHubPathInfoBuilder()
                .hubPathRequestDto(requestDto)
                .departureHub(departureHub)
                .arrivalHub(arrivalHub)
                .build();

        hubPathRepository.save(hubPath);
        return HubPathResponseDto.of(hubPath);
    }


    @Transactional
    public HubPathResponseDto updateHubPath(UUID hubPathId, HubPathRequestDto requestDto) throws HubException {
        HubPath hubPath = hubPathRepository.findByHubPathId(hubPathId)
                .orElseThrow(() -> new HubException(ApiResultError.HUB_PATH_NO_EXIST));

        Hub newDepartureHub = hubService.findHubById(requestDto.getDepartureHubId());
        Hub newArrivalHub = hubService.findHubById(requestDto.getArrivalHubId());

        hubPath.update(requestDto, newDepartureHub, newArrivalHub);

        return HubPathResponseDto.of(hubPath);
    }

    @Transactional
    public void deleteHubPath(UUID hubPathId) throws HubException {
        HubPath hubPath = hubPathRepository.findByHubPathId(hubPathId)
                .orElseThrow(() -> new HubException(ApiResultError.HUB_PATH_NO_EXIST));

        hubPath.softDelete();
        hubPathRepository.save(hubPath);
    }

    @Transactional(readOnly = true)
    public HubPathResponseDto getHubPath(UUID hubPathId) throws HubException {
        HubPath hubPath = hubPathRepository.findByHubPathId(hubPathId)
                .orElseThrow(() -> new HubException(ApiResultError.HUB_PATH_NO_EXIST));

        return HubPathResponseDto.of(hubPath);
    }

    public List<HubPathResponseDto> getHubPathList(UUID departureHubId, UUID arrivalHubId) throws HubException {

        Hub departureHub = hubService.findHubById(departureHubId);
        Hub arrivalHub = hubService.findHubById(arrivalHubId);

        boolean isForward = departureHub.getSequence() < arrivalHub.getSequence();

        List<HubPath> paths;
        if (isForward) {
            paths = hubPathRepository.findPathsBetweenHubs(departureHub, arrivalHub);
        } else {
            paths = hubPathRepository.findPathsBetweenHubsReverse(departureHub, arrivalHub);
        }

        List<HubPathResponseDto> result = new ArrayList<>();

        if (isForward) {
            for (HubPath path : paths) {
                result.add(HubPathResponseDto.of(path));
            }
        } else {
            for (int i = paths.size() - 1; i >= 0; i--) {
                result.add(HubPathResponseDto.ofReverse(paths.get(i)));
            }
        }

        // 마지막 허브 추가 (도착 허브)
        result.add(HubPathResponseDto.ofLastHub(arrivalHub));

        return result;
    }

    @Transactional(readOnly = true)
    public Page<HubPathResponseDto> searchHubPaths(UUID departureHubId, UUID arrivalHubId, Long minDuration, Long maxDuration, Pageable pageable) throws HubException {
        Page<HubPath> hubPaths = hubPathRepository.searchPaths(departureHubId, arrivalHubId, minDuration, maxDuration, pageable);
        return hubPaths.map(HubPathResponseDto::of);
    }
}
