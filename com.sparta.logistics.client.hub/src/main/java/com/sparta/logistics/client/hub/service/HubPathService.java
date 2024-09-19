package com.sparta.logistics.client.hub.service;

import com.sparta.logistics.client.hub.client.AiClient;
import com.sparta.logistics.client.hub.client.dto.AiRequestDto;
import com.sparta.logistics.client.hub.common.exception.HubException;
import com.sparta.logistics.client.hub.dto.HubPathRequestDto;
import com.sparta.logistics.client.hub.dto.HubPathResponseDto;
import com.sparta.logistics.client.hub.model.Hub;
import com.sparta.logistics.client.hub.model.HubPath;
import com.sparta.logistics.client.hub.repository.HubPathRepository;
import com.sparta.logistics.common.model.ApiResult;
import com.sparta.logistics.common.type.ApiResultError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HubPathService {


    private final HubService hubService;
    private final HubPathRepository hubPathRepository;
    private final AiClient aiClient;

    @CacheEvict(cacheNames = "hubPathAllCache", allEntries = true)
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


    @CachePut(cacheNames = "hubPathCache", key = "#hubPathId")
    @CacheEvict(cacheNames = "hubPathAllCache", allEntries = true)
    @Transactional
    public HubPathResponseDto updateHubPath(UUID hubPathId, HubPathRequestDto requestDto) throws HubException {
        HubPath hubPath = hubPathRepository.findByHubPathIdWithHubs(hubPathId)
                .orElseThrow(() -> new HubException(ApiResultError.HUB_PATH_NO_EXIST));

        Hub newDepartureHub = hubService.findHubById(requestDto.getDepartureHubId());
        Hub newArrivalHub = hubService.findHubById(requestDto.getArrivalHubId());

        hubPath.update(requestDto, newDepartureHub, newArrivalHub);

        return HubPathResponseDto.of(hubPath);
    }

    @CacheEvict(cacheNames = {"hubPathCache", "hubPathAllCache"}, key = "#hubPathId")
    @Transactional
    public void deleteHubPath(UUID hubPathId, String userId) throws HubException {
        HubPath hubPath = hubPathRepository.findByHubPathIdWithHubs(hubPathId)
                .orElseThrow(() -> new HubException(ApiResultError.HUB_PATH_NO_EXIST));

        hubPath.softDelete(userId);
        hubPathRepository.save(hubPath);
    }

    @Cacheable(cacheNames = "hubPathCache", key = "#hubPathId")
    @Transactional(readOnly = true)
    public HubPathResponseDto getHubPath(UUID hubPathId) throws HubException {
        HubPath hubPath = hubPathRepository.findByHubPathIdWithHubs(hubPathId)
                .orElseThrow(() -> new HubException(ApiResultError.HUB_PATH_NO_EXIST));

        return HubPathResponseDto.of(hubPath);
    }

    @Cacheable(cacheNames = "hubPathAllCache", key = "getMethodName()")
    @Transactional(readOnly = true)
    public List<HubPathResponseDto> getAllHubPaths() throws HubException {
        List<HubPath> hubPaths = hubPathRepository.findAllByIsDeletedFalse();
        return hubPaths.stream()
                .map(HubPathResponseDto::of)
                .collect(Collectors.toList());
    }


    @Cacheable(cacheNames = "hubPathListCache", key = "{#departureHubId, #arrivalHubId}")
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

    public ApiResult getOptimalHubPath(UUID departureHubId, UUID arrivalHubId) throws HubException {
        List<HubPathResponseDto> paths = getHubPathList(departureHubId, arrivalHubId);

        // AI요청을 위한 데이터 준비
        String pathsContent = convertPathsToSimplifiedString(paths);

        AiRequestDto aiRequestDto = new AiRequestDto();
        aiRequestDto.setService("hub-service");
        String preQuestion = "교통 수단: 자동차, 목적: 시간 단축, 기준: 주소 간 현재 예상 소요시간\n";
        String instructions = "1. 주어진 정보만을 사용하여 판단해주세요.\n" +
                "2. 각 경로의 소요 시간을 고려하여 최적의 경로를 선택해주세요.\n" +
                "3. 최적 경로는 허브 이름으로 경로만 응답해주세요.\n";
        String question = preQuestion + instructions + "다음 허브 이동 경로 리스트 중에서 최적의 경로를 선택해주세요 :\n\n" + pathsContent;
        log.info("@@{}", pathsContent);

        aiRequestDto.setQuestion(question);


        // AI 서비스 호출 및 결과 반환
        return aiClient.createAI(aiRequestDto);
    }

    private String convertPathsToSimplifiedString(List<HubPathResponseDto> paths) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < paths.size(); i++) {
            HubPathResponseDto path = paths.get(i);
            sb.append(String.format("%d. %s (%s)", i + 1, path.getHubName(), path.getAddress()));

            if (i < paths.size() - 1) {
                sb.append(" -> ");
                sb.append(String.format("%s (%s)", path.getNextHubName(), path.getNextHubAddress()));
                sb.append(String.format(", 예상 소요 시간: %d분", path.getDuration()));
            }
            sb.append("\n");
        }
        return sb.toString();
    }


}
