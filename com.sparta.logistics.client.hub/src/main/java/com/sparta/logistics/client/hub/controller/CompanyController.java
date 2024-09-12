package com.sparta.logistics.client.hub.controller;

import com.sparta.logistics.client.hub.common.exception.HubException;
import com.sparta.logistics.client.hub.dto.CompanyRequestDto;
import com.sparta.logistics.client.hub.dto.CompanyResponseDto;
import com.sparta.logistics.client.hub.enums.CompanyType;
import com.sparta.logistics.client.hub.model.validation.CompanyVaild0001;
import com.sparta.logistics.client.hub.service.CompanyService;
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
@RequiredArgsConstructor
@RequestMapping("/companies")
public class CompanyController extends CustomApiController {

    // TODO : Search, 권한 관리 ,userId

    private final CompanyService companyService;

    @PostMapping
    public ApiResult createCompany(@RequestBody @Validated({CompanyVaild0001.class}) CompanyRequestDto requestDto, Errors errors
    ) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        if (errors.hasErrors()) {
            return bindError(errors, apiResult);
        }

        try {
            CompanyResponseDto responseDto = companyService.createCompany(requestDto);
            apiResult.set(ApiResultError.NO_ERROR).setResultData(responseDto);
        } catch (HubException e) {
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }
        return apiResult;
    }

    @GetMapping("/{companyId}")
    public ApiResult getCompany(@PathVariable UUID companyId) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        try {
            CompanyResponseDto responseDto = companyService.getCompany(companyId);
            apiResult.set(ApiResultError.NO_ERROR).setResultData(responseDto);
        } catch (HubException e) {
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }
        return apiResult;
    }

    @GetMapping
    public ApiResult getAllCompanies() {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        try {
            List<CompanyResponseDto> responseDtoList = companyService.getAllCompanies();
            apiResult.set(ApiResultError.NO_ERROR).setResultData(responseDtoList);
        } catch (HubException e) {
            apiResult.set(ApiResultError.ERROR_DEFAULT).setResultMessage(e.getMessage());
        }
        return apiResult;
    }

    @PatchMapping("/{companyId}")
    public ApiResult updateCompany(@PathVariable UUID companyId, @RequestBody CompanyRequestDto requestDto) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        try {
            CompanyResponseDto responseDto = companyService.updateCompany(companyId, requestDto);
            apiResult.set(ApiResultError.NO_ERROR).setResultData(responseDto);
        } catch (HubException e) {
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }
        return apiResult;
    }

    @DeleteMapping("/{companyId}")
    public ApiResult deleteCompany(@PathVariable UUID companyId) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        try {
            companyService.deleteCompany(companyId);
            apiResult.set(ApiResultError.NO_ERROR).setResultMessage("삭제되었습니다.");
        } catch (HubException e) {
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }
        return apiResult;
    }

    @GetMapping("/search")
    public ApiResult searchCompanies(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) CompanyType companyType,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) UUID managingHubId) {
        // TODO : 권한 관리 : 자신의 업체만 수정 가능, 다른 업체의 읽기 & 검색만 가능

        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        try {
            List<CompanyResponseDto> searchResult = companyService.searchCompanies(name, companyType, address, managingHubId);
            apiResult.set(ApiResultError.NO_ERROR).setResultData(searchResult);
        } catch (HubException e) {
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }
        return apiResult;
    }
}
