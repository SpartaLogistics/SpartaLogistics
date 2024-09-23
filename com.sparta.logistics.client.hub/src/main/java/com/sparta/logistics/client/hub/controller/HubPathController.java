package com.sparta.logistics.client.hub.controller;

import com.sparta.logistics.client.hub.common.exception.HubException;
import com.sparta.logistics.client.hub.dto.HubPathRequestDto;
import com.sparta.logistics.client.hub.dto.HubPathResponseDto;
import com.sparta.logistics.client.hub.model.validation.HubPathValid0001;
import com.sparta.logistics.client.hub.service.HubPathService;
import com.sparta.logistics.common.controller.CustomApiController;
import com.sparta.logistics.common.model.ApiResult;
import com.sparta.logistics.common.model.RoleCheck;
import com.sparta.logistics.common.type.ApiResultError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hub-paths")
@Tag(name = "HubPath 허브 이동 경로 요청 API", description = "HubPath API Docs")
public class HubPathController extends CustomApiController {

    private final HubPathService hubPathService;

    @RoleCheck(roles = "MASTER")
    @PostMapping
    @Operation(summary = "허브 이동 경로 생성 API", description = "허브 이동 경로를 생성합니다.")
    public ApiResult createHubPath(
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
    @Operation(summary = "허브 조회 API", description = "삭제되지 않은 허브 이동 경로를 조회합니다.")
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
    @Operation(summary = "허브 이동 경로 목록 조회 API", description = "삭제되지 않은 허브 이동 경로 목록을 조회합니다.")
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


    @RoleCheck(roles = "MASTER")
    @PatchMapping("/{hubPathId}")
    @Operation(summary = "허브 이동 경로 수정 API", description = "삭제되지 않은 허브 이동 경로를 수정합니다.")
    public ApiResult updateHubPath(@RequestBody HubPathRequestDto requestDto, @PathVariable UUID hubPathId) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        try {
            HubPathResponseDto responseDto = hubPathService.updateHubPath(hubPathId, requestDto);
            apiResult.set(ApiResultError.NO_ERROR).setResultData(responseDto);
        } catch (HubException e) {
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }
        return apiResult;
    }

    @RoleCheck(roles = "MASTER")
    @DeleteMapping("/{hubPathId}")
    @Operation(summary = "허브 이동 경로 삭제 API", description = "허브 이동 경로를 삭제합니다. (논리적 삭제)")
    public ApiResult deleteHubPath(@PathVariable UUID hubPathId, @RequestHeader("X-User-Id") String userId) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        try {
            hubPathService.deleteHubPath(hubPathId, userId);
            apiResult.set(ApiResultError.NO_ERROR).setResultMessage("삭제되었습니다.");
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
    @Operation(summary = "허브 이동 경로 검색 API",
            description = "출발 허브 ID(departureHubId), 도착 허브 ID(arrivalHubId), 최소 소요시간(minDuration), 최대 소요시간(maxDuration)를 기준으로 허브 이동 경로를 검색합니다.")
    public ApiResult searchHubPath(
            @RequestParam(required = false) UUID departureHubId,
            @RequestParam(required = false) UUID arrivalHubId,
            @RequestParam(required = false) Long minDuration,
            @RequestParam(required = false) Long maxDuration,
            @RequestParam(defaultValue = "createdAt,desc") String sort,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "0") int page
    ) {
        try {
            String[] sortParams = sort.split(",");
            String sortBy = sortParams[0];
            String direction = sortParams.length > 1 ? sortParams[1] : "desc";

            if (!sortBy.equals("createdAt") && !sortBy.equals("updatedAt") && !sortBy.equals("duration")) {
                sortBy = "createdAt";
            }

            Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            int validSize = (size == 30 || size == 50) ? size : 10;

            Pageable pageable = PageRequest.of(page, validSize, Sort.by(sortDirection, sortBy));
            Page<HubPathResponseDto> paths = hubPathService.searchHubPaths(departureHubId, arrivalHubId, minDuration, maxDuration, pageable);

            return new ApiResult(ApiResultError.NO_ERROR).setResultData(paths);
        } catch (Exception e) {
            return new ApiResult(ApiResultError.ERROR_PARAMETERS).setResultMessage(e.getMessage());
        }
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
    @Operation(summary = "허브 이동 경로 리스트 조회 API", description = "출발 허브 ID(departureHubId) 도착 허브 ID(arrivalHubId)간의 허브 리스트를 조회합니다.")
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

    @GetMapping("/optimal")
    @Operation(summary = "허브 이동 최적 경로 AI 조회 API", description = "출발 허브 ID(departureHubId) 도착 허브 ID(arrivalHubId)간의 허브 리스트를 AI에게 요청하여 최적의 경로를 응답받습니다.")
    public ApiResult getOptimalHubPath(@RequestParam UUID departureHubId, @RequestParam UUID arrivalHubId) {
        try {
            return hubPathService.getOptimalHubPath(departureHubId, arrivalHubId);
        } catch (HubException e) {
            return new ApiResult(e.getCode()).setResultMessage(e.getMessage());
        }
    }


}
