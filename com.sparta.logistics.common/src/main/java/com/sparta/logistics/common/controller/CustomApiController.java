package com.sparta.logistics.common.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.logistics.common.model.ApiResult;
import com.sparta.logistics.common.model.UserVO;
import com.sparta.logistics.common.type.ApiResultError;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;


@Slf4j
@RequiredArgsConstructor
public abstract class CustomApiController {

    protected ObjectMapper mapper = new ObjectMapper();

    /**
     * <p>[공통] Parameter Bind error 처리
     * </p>
     * @param errors
     * @return
     */
    protected final ApiResult bindError(Errors errors) {
        return bindError(errors, null);
    }

    /**
     * <p>[공통] Parameter Bind error 처리
     * 파라미터 bind 오류내역 리턴
     * </p>
     * @param errors
     * @param orgAxRet ApiResult이 null이 아니면 넘겨준 ApiResult에 설정
     * @return
     */
    protected final ApiResult bindError(Errors errors, ApiResult orgAxRet) {
        ApiResult axRet = orgAxRet == null ? new ApiResult() : orgAxRet;
        log.debug("errors:{}", errors);
        String dfErrMsg = errors.getAllErrors().get(0).getDefaultMessage();

        return axRet.set(ApiResultError.ERROR_PARAMETERS, dfErrMsg);
    }

    /**
     * <p>[공통] Parameter Bind error 처리
     * 파라미터 bind 오류내역 리턴
     * </p>
     * @param e
     * @param orgAxRet ApiResult이 null이 아니면 넘겨준 ApiResult에 설정
     * @return
     */
    protected final ApiResult bindApiError(ConstraintViolationException e, ApiResult orgAxRet) {
        ApiResult axRet = orgAxRet == null ? new ApiResult() : orgAxRet;
        log.debug("errors:{}", e.getMessage());
        String dfErrMsg = e.getConstraintViolations().iterator().next().getMessage();

        return axRet.set(ApiResultError.ERROR_INTERNAL_API_PARAMETERS, dfErrMsg);
    }


}

