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
    public ApiResult getAllHubPaths(Pageable pageable) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        try {
            Page<HubPathResponseDto> hubPaths = hubPathService.getAllHubPaths(pageable);
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
