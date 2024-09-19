package com.sparta.logistics.client.ai.controller;

import com.sparta.logistics.client.ai.common.exception.AIException;
import com.sparta.logistics.client.ai.dto.AIRequestDto;
import com.sparta.logistics.client.ai.dto.AIResponseDto;
import com.sparta.logistics.client.ai.dto.AISearchCriteria;
import com.sparta.logistics.client.ai.service.AIService;
import com.sparta.logistics.common.controller.CustomApiController;
import com.sparta.logistics.common.model.ApiResult;
import com.sparta.logistics.common.type.ApiResultError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
@Tag(name = "AI 요청 API", description = "AI 요청 내역 관리 목적의 API Docs")
public class AIController extends CustomApiController {

    private final AIService aiService;

    /**
     * AI 생성
     * @param aiRequestDto
     * @param errors
     * @return
     */
    @Operation(summary = "AI 생성", description = "AI 생성")
    @PostMapping
    public ApiResult createAI(@RequestBody @Validated AIRequestDto aiRequestDto,
                              Errors errors) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        if (errors.hasErrors()) {
            return bindError(errors, apiResult);
        }

        try {
            AIResponseDto aiResponseDto = aiService.createAI(aiRequestDto);
            apiResult.set(ApiResultError.NO_ERROR).setResultData(aiResponseDto);
        } catch (AIException e) {
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }

        return apiResult;
    }

    /**
     * AI 상세 조회
     * @param aiId
     * @return
     */
    @Operation(summary = "AI 상세 조회", description = "AI 상세 조회")
    @GetMapping("/{aiId}")
    public ApiResult getAI(@PathVariable UUID aiId) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);

        try {
            AIResponseDto aiResponseDto = aiService.getAI(aiId);
            apiResult.set(ApiResultError.NO_ERROR).setResultData(aiResponseDto);
        } catch (AIException e) {
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }

        return apiResult;
    }

    /**
     * AI 목록 조회
     * @param aiSearchCriteria
     * @param pageable
     * @return
     */
    @Operation(summary = "AI 목록 조회", description = "AI 목록 조회")
    @GetMapping
    public ApiResult getAIList(AISearchCriteria aiSearchCriteria,
                               @PageableDefault Pageable pageable) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);

        Page<AIResponseDto> list = aiService.getAIList(aiSearchCriteria, pageable);

        return apiResult.set(ApiResultError.NO_ERROR).setList(list).setPageInfo(list);
    }

    /**
     * AI 삭제
     * @param aiId
     * @return
     */
    @Operation(summary = "AI 삭제", description = "AI 삭제")
    @DeleteMapping("/{aiId}")
    public ApiResult deleteAI(@PathVariable("aiId") UUID aiId) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);

        try {
            aiService.deleteAI(aiId);
            apiResult.set(ApiResultError.NO_ERROR).setResultMessage("삭제되었습니다.");
        } catch (AIException e) {
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }
        return apiResult;
    }


}
