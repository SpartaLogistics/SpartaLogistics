package com.sparta.logistics.client.hub.controller;

import com.sparta.logistics.client.hub.common.exception.HubException;
import com.sparta.logistics.client.hub.dto.CompanyRequestDto;
import com.sparta.logistics.client.hub.dto.CompanyResponseDto;
import com.sparta.logistics.client.hub.enums.CompanyType;
import com.sparta.logistics.client.hub.model.validation.CompanyVaild0001;
import com.sparta.logistics.client.hub.service.CompanyService;
import com.sparta.logistics.common.controller.CustomApiController;
import com.sparta.logistics.common.model.ApiResult;
import com.sparta.logistics.common.model.RoleCheck;
import com.sparta.logistics.common.type.ApiResultError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Company 업체 요청 API", description = "Company API Docs")
@RequestMapping("/companies")
public class CompanyController extends CustomApiController {

    private final CompanyService companyService;

    @RoleCheck(roles = {"MASTER", "OWNER"})
    @PostMapping
    @Operation(summary = "업체 생성 API", description = "업체를 생성합니다.")
    public ApiResult createCompany(@RequestBody @Validated({CompanyVaild0001.class}) CompanyRequestDto requestDto,
                                   Errors errors,
                                   @RequestHeader("X-User-Name") String username
    ) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        if (errors.hasErrors()) {
            return bindError(errors, apiResult);
        }

        try {
            CompanyResponseDto responseDto = companyService.createCompany(requestDto, username);
            apiResult.set(ApiResultError.NO_ERROR).setResultData(responseDto);
        } catch (HubException e) {
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }
        return apiResult;
    }

    @GetMapping("/{companyId}")
    @Operation(summary = "업체 조회 API", description = "삭제되지 않은 특정 업체를 조회합니다.")
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
    @Operation(summary = "업체 목록 조회 API", description = "삭제되지 않은 업체 목록을 조회합니다.")
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
    @Operation(summary = "업체 수정 API", description = "삭제되지 않은 업체 목록을 수정합니다.")
    public ApiResult updateCompany(@PathVariable UUID companyId,
                                   @RequestBody CompanyRequestDto requestDto,
                                   @RequestHeader("X-User-Name") String username) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        try {
            CompanyResponseDto responseDto = companyService.updateCompany(companyId, requestDto, username);
            apiResult.set(ApiResultError.NO_ERROR).setResultData(responseDto);
        } catch (HubException e) {
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }
        return apiResult;
    }

    @RoleCheck(roles = {"MASTER", "OWNER"})
    @DeleteMapping("/{companyId}")
    @Operation(summary = "업체 삭제 API", description = "업체를 삭제합니다. (논리적 삭제)")
    public ApiResult deleteCompany(@PathVariable UUID companyId,
                                   @RequestHeader("X-User-Name") String username,
                                   @RequestHeader("X-User-Id") String userId) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        try {
            companyService.deleteCompany(companyId, username, userId);
            apiResult.set(ApiResultError.NO_ERROR).setResultMessage("삭제되었습니다.");
        } catch (HubException e) {
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }
        return apiResult;
    }

    @GetMapping("/search")
    @Operation(summary = "업체 검색 API", description = "업체명, 업체 타입, 주소, 관리 허브 ID를 기준으로 업체를 검색합니다.")
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
