package com.sparta.logistics.client.user.controller;

import com.sparta.logistics.client.user.common.exception.UserException;
import com.sparta.logistics.client.user.dto.DeliveryManagerResponseDto;
import com.sparta.logistics.client.user.dto.ManagerRequestDto;
import com.sparta.logistics.client.user.dto.UpdateManagerDto;
import com.sparta.logistics.client.user.model.User;
import com.sparta.logistics.client.user.model.validation.DeliveryManagerValid0001;
import com.sparta.logistics.client.user.model.validation.DeliveryManagerValid0002;
import com.sparta.logistics.client.user.repository.UserRepository;
import com.sparta.logistics.client.user.service.DeliveryManagerService;
import com.sparta.logistics.common.controller.CustomApiController;
import com.sparta.logistics.common.model.ApiResult;
import com.sparta.logistics.common.type.ApiResultError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/delivery_managers")
@Tag(name = "DeliveryManager 요청 API", description = "DeliveryManager 요청 내역 관리 목적의 API Docs")
@RequiredArgsConstructor
@Slf4j
public class DeliveryManagerController extends CustomApiController {
    private final DeliveryManagerService deliveryManagerService;
    private final UserRepository userRepository;

    @Operation(summary = "DeliveryManager 생성", description = "deliveryManager 생성")
    @PostMapping
    public ApiResult createDeliveryManager(@RequestBody @Validated({DeliveryManagerValid0001.class}) ManagerRequestDto requestDto,
                                           @RequestHeader("X-User-Name") String username, Errors errors) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        log.info(username);
        if(errors.hasErrors()) {
            return bindError(errors, apiResult);
        }
        try{
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            DeliveryManagerResponseDto response = deliveryManagerService.createDeliveryManager(requestDto, user.getId());
            apiResult.set(ApiResultError.NO_ERROR).setResultData(response);
        } catch (UserException e){
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }
       return apiResult;
    }

    @Operation(summary = "DeliveryManager 조회", description = "모든 DeliveryManager 조회")
    @GetMapping
    public ApiResult getAllDeliveryManagers() {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        try{
            List<DeliveryManagerResponseDto> response = deliveryManagerService.getAllDeliveryManagers();
            apiResult.set(ApiResultError.NO_ERROR).setResultData(response);
        } catch (UserException e){
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }
        return apiResult;
    }

    @Operation(summary = "DeliveryManagerId로 유저 검색", description = "DeliveryManagerId로 유저 검색")
    @GetMapping("/{deliveryId}")
    public ApiResult getDeliveryManagerById(@PathVariable("deliveryId") UUID deliveryId) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        try{
            DeliveryManagerResponseDto response = deliveryManagerService.getDeliveryManagerById(deliveryId);
            apiResult.set(ApiResultError.NO_ERROR).setResultData(response);
        } catch (UserException e){
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }
        return apiResult;
    }

    @Operation(summary = "DeliveryManager 수정", description = "DeliveryManager 수정")
    @PatchMapping("/{deliveryId}")
    public ApiResult patchDeliveryManager(@PathVariable("deliveryId") UUID deliveryId, @RequestBody @Validated({DeliveryManagerValid0002.class}) UpdateManagerDto requestDto, Errors errors) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        if(errors.hasErrors()) {
            return bindError(errors, apiResult);
        }
        try{
            DeliveryManagerResponseDto response = deliveryManagerService.patchDeliveryManager(deliveryId, requestDto);
            apiResult.set(ApiResultError.NO_ERROR).setResultData(response);
        } catch (UserException e){
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }
        return apiResult;
    }

    @Operation(summary = "DeliveryManager 삭제", description = "DeliveryManager 삭제")
    @DeleteMapping("/{deliveryId}")
    public ApiResult deleteDeliveryManager(@PathVariable("deliveryId") UUID deliveryId, @RequestHeader("X-User-Name") String username) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        try{
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            DeliveryManagerResponseDto response = deliveryManagerService.deleteDeliveryManager(deliveryId, user.getId());
            apiResult.set(ApiResultError.NO_ERROR).setResultData(response);
        } catch (UserException e){
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }
       return apiResult;
    }
}
