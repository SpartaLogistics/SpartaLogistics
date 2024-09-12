package com.sparta.logistics.client.hub.controller;

import com.sparta.logistics.client.hub.common.exception.HubException;
import com.sparta.logistics.client.hub.dto.ProductDetailResponseDto;
import com.sparta.logistics.client.hub.dto.ProductRequestDto;
import com.sparta.logistics.client.hub.dto.ProductResponseDto;
import com.sparta.logistics.client.hub.model.validation.ProductValid0001;
import com.sparta.logistics.client.hub.service.ProductService;
import com.sparta.logistics.common.controller.CustomApiController;
import com.sparta.logistics.common.model.ApiResult;
import com.sparta.logistics.common.type.ApiResultError;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController extends CustomApiController {

    private final ProductService productService;

    @PostMapping
    public ApiResult createProduct(@RequestBody @Validated({ProductValid0001.class}) ProductRequestDto requestDto, Errors errors) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        if (errors.hasErrors()) {
            return bindError(errors, apiResult);
        }

        try {
            ProductResponseDto responseDto = productService.createProduct(requestDto);
            apiResult.set(ApiResultError.NO_ERROR).setResultData(responseDto);
        } catch (HubException e) {
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }

        return apiResult;
    }

    @PatchMapping("/{productId}")
    public ApiResult updateProduct(@PathVariable UUID productId,
                                   @RequestBody ProductRequestDto requestDto,
                                   Errors errors) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        if (errors.hasErrors()) {
            return bindError(errors, apiResult);
        }

        try {
            ProductResponseDto responseDto = productService.updateProduct(productId, requestDto);
            apiResult.set(ApiResultError.NO_ERROR).setResultData(responseDto);
        } catch (HubException e) {
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }

        return apiResult;
    }

    @GetMapping("/{productId}")
    public ApiResult getProduct(@PathVariable UUID productId) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);

        try {
            ProductDetailResponseDto responseDto = productService.getProduct(productId);
            apiResult.set(ApiResultError.NO_ERROR).setResultData(responseDto);
        } catch (HubException e) {
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }

        return apiResult;
    }

    @GetMapping
    public ApiResult getAllProducts(Pageable pageable) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);

        try {
            Page<ProductResponseDto> responseDtoPage = productService.getAllProducts(pageable);
            apiResult.set(ApiResultError.NO_ERROR).setResultData(responseDtoPage);
        } catch (HubException e) {
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }

        return apiResult;
    }

    @DeleteMapping("/{productId}")
    public ApiResult deleteProduct(@PathVariable UUID productId) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);

        try {
            productService.deleteProduct(productId);
            apiResult.set(ApiResultError.NO_ERROR).setResultMessage("삭제되었습니다.");
        } catch (HubException e) {
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }

        return apiResult;
    }

}
