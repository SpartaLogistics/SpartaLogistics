package com.sparta.logistics.client.hub.controller;

import com.sparta.logistics.client.hub.common.exception.HubException;
import com.sparta.logistics.client.hub.dto.HubRequestDto;
import com.sparta.logistics.client.hub.dto.HubResponseDto;
import com.sparta.logistics.client.hub.model.validation.HubValid0001;
import com.sparta.logistics.client.hub.model.validation.HubValid0002;
import com.sparta.logistics.client.hub.service.HubService;
import com.sparta.logistics.common.controller.CustomApiController;
import com.sparta.logistics.common.model.ApiResult;
import com.sparta.logistics.common.type.ApiResultError;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/hubs")
@RequiredArgsConstructor
public class HubController extends CustomApiController {

    private final HubService hubService;

    // 허브 생성 API
    @PostMapping
    public ApiResult createHub(@RequestBody @Validated({HubValid0001.class}) HubRequestDto requestDto, Errors errors) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        if (errors.hasErrors()) {
            return bindError(errors, apiResult);
        }

        // DTO를 서비스로 전달하여 허브 생성
        try {
            HubResponseDto responseDto = hubService.createHub(requestDto);
            apiResult.set(ApiResultError.NO_ERROR).setResultData(responseDto);
        } catch (HubException e) {
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }
        return apiResult;
    }

    // 허브 목록 조회 API
    @GetMapping
    public ApiResult getAllHubs() {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        try {
            List<HubResponseDto> responseDtoList = hubService.getAllHubs();
            apiResult.set(ApiResultError.NO_ERROR).setResultData(responseDtoList);
        } catch (HubException e) {
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }
        return apiResult;
    }


    // 허브 단건 조회 API
    @GetMapping("/{hubId}")
    public ApiResult getHubById(@PathVariable UUID hubId) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        try {
            HubResponseDto responseDto = hubService.getHubById(hubId);
            apiResult.set(ApiResultError.NO_ERROR).setResultData(responseDto);
        } catch (HubException e) {
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }
        return apiResult;
    }

    //허브 수정 API
    @PatchMapping("/{id}")
    public ApiResult updateHub(
            @PathVariable("id") UUID hubID,
            @RequestBody @Validated({HubValid0002.class}) HubRequestDto requestDto,
            Errors errors
    ) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);

        if (errors.hasErrors()) {
            return bindError(errors, apiResult);
        }

        try {
            HubResponseDto responseDto = hubService.updateHub(hubID, requestDto);
            apiResult.set(ApiResultError.NO_ERROR).setResultData(responseDto);
        } catch (HubException e) {
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }
        return apiResult;
    }


    // 허브 삭제 API
    @DeleteMapping("/{id}")
    public ApiResult deleteHub(@PathVariable("id") UUID hubId) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        try {
            hubService.deleteHub(hubId);
            apiResult.set(ApiResultError.NO_ERROR).setResultMessage("삭제되었습니다.");
        } catch (HubException e) {
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }
        return apiResult;
    }

    @GetMapping("/search")
    public ApiResult searchHubs(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address
    ) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        try {
            List<HubResponseDto> searchResult = hubService.searchHubs(name, address);
            apiResult.set(ApiResultError.NO_ERROR).setResultData(searchResult);
        } catch (HubException e) {
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }
        return apiResult;
    }

}
