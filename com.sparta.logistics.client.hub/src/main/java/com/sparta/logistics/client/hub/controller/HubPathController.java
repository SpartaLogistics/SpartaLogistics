package com.sparta.logistics.client.hub.controller;

import com.sparta.logistics.client.hub.common.exception.HubException;
import com.sparta.logistics.client.hub.dto.HubPathRequestDto;
import com.sparta.logistics.client.hub.dto.HubPathResponseDto;
import com.sparta.logistics.client.hub.model.validation.HubPathValid0001;
import com.sparta.logistics.client.hub.service.HubPathService;
import com.sparta.logistics.common.controller.CustomApiController;
import com.sparta.logistics.common.model.ApiResult;
import com.sparta.logistics.common.type.ApiResultError;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/hub-paths")
@RequiredArgsConstructor
public class HubPathController extends CustomApiController {

    private final HubPathService hubPathService;

    @PostMapping
    public ApiResult createHunPath(
            @RequestBody @Validated({HubPathValid0001.class}) HubPathRequestDto requestDto,
            Errors errors) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);

        if (errors.hasErrors()) {
            return bindError(errors, apiResult);
        }

        try {
            HubPathResponseDto responseDto = hubPathService.createHubPath(requestDto);
            apiResult.set(ApiResultError.NO_ERROR).setResultData(responseDto);
        } catch (HubException e) {
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }
        return apiResult;
    }

    @GetMapping("/{hubPathId}")
    public ApiResult getHubPath(@PathVariable UUID hubPathId) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        try {
            HubPathResponseDto responseDto = hubPathService.getHubPath(hubPathId);
            apiResult.set(ApiResultError.NO_ERROR).setResultData(responseDto);
        } catch (HubException e) {
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }
        return apiResult;
    }

    @GetMapping
    public ApiResult getAllHubPaths() {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        try {
            List<HubPathResponseDto> hubPaths = hubPathService.getAllHubPaths();
            apiResult.set(ApiResultError.NO_ERROR).setResultData(hubPaths);
        } catch (HubException e) {
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }
        return apiResult;
    }


    @PatchMapping("/{hubPathId}")
    public ApiResult updateHubPath(@RequestBody HubPathRequestDto requsetDto, @PathVariable UUID hubPathId) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        try {
            HubPathResponseDto responseDto = hubPathService.updateHubPath(hubPathId, requsetDto);
            apiResult.set(ApiResultError.NO_ERROR).setResultData(responseDto);
        } catch (HubException e) {
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }
        return apiResult;
    }

    @DeleteMapping("/{hubPathId}")
    public ApiResult deleteHubPath(@PathVariable UUID hubPathId) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        try {
            hubPathService.deleteHubPath(hubPathId);
            apiResult.set(ApiResultError.NO_ERROR);
        } catch (HubException e) {
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }
        return apiResult;
    }

    /**
     * 허브 이동 경로 검색 API
     *
     * @param departureHubId 출발 허브의 UUID (옵션)
     * @param arrivalHubId   도착 허브의 UUID (옵션)
     * @param minDuration    최소 소요시간 (ex: 100 : 100이상인 경로 검색)
     * @param maxDuration    최대 소요시간 (ex: 100 : 100이하인 경로 검색)
     */
    @GetMapping("/search")
    public ApiResult searchHubPath(
            @RequestParam(required = false) UUID departureHubId,
            @RequestParam(required = false) UUID arrivalHubId,
            @RequestParam(required = false) Long minDuration,
            @RequestParam(required = false) Long maxDuration,
            Pageable pageable
    ) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        try {
            Page<HubPathResponseDto> paths = hubPathService.searchHubPaths(departureHubId, arrivalHubId, minDuration, maxDuration, pageable);
            apiResult.set(ApiResultError.NO_ERROR).setResultData(paths);
        } catch (HubException e) {
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }
        return apiResult;
    }


    /**
     * 허브 간 경로 리스트 조회 API
     *
     * @param departureHubId 출발 허브의 UUID (필수)
     * @param arrivalHubId   도착 허브의 UUID (필수)
     * @return ApiResult 허브 경로 리스트를 담은 ApiResult 객체
     * @description 출발 허브 ID와 도착 허브 ID를 입력받아 해당하는 허브 경로 리스트를 조회
     * 정상 조회 시 ApiResult에 NO_ERROR 코드와 함께 허브 경로 리스트가 반환
     * 허브 경로 조회 실패 시 HubException을 처리하며, ApiResult에 에러 코드와 에러 메시지가 포함
     */
    @GetMapping("/hubList")
    public ApiResult getHubPathList(
            @RequestParam UUID departureHubId,
            @RequestParam UUID arrivalHubId) {

        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        try {

            List<HubPathResponseDto> paths = hubPathService.getHubPathList(departureHubId, arrivalHubId);
            apiResult.set(ApiResultError.NO_ERROR).setResultData(paths);
        } catch (HubException e) {
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }

        return apiResult;
    }


}
