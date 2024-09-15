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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
@Tag(name = "Product 상품 요청 API", description = "Product API Docs")
public class ProductController extends CustomApiController {

    // TODO : 권한 관리

    private final ProductService productService;

    @PostMapping
    @Operation(summary = "상품 생성 API", description = "상품을 생성합니다.")
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
    @Operation(summary = "상품 수정 API", description = "삭제되지 않은 상품을 수정합니다.")
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
    @Operation(summary = "상품 조회 API", description = "삭제되지 않은 상품을 조회합니다.")
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
    @Operation(summary = "상품 목록 조회 API", description = "삭제되지 않은 상품 목록을 조회합니다.")
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
    @Operation(summary = "상품 삭제 API", description = "상품을 삭제합니다. (논리적 삭제)")
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

    @GetMapping("/search")
    @Operation(summary = "상품 검색 API", description = "상품명(name), 상품 업체(companyId)를 기준으로 상품을 조회합니다.")
    public ApiResult searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) UUID companyId
    ) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        try {
            List<ProductResponseDto> searchResult = productService.searchProducts(name, companyId);
            apiResult.set(ApiResultError.NO_ERROR).setResultData(searchResult);
        } catch (HubException e) {
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }
        return apiResult;
    }

    @PatchMapping("/external/increase/{productId}")
    @Operation(summary = "상품 수량 증가 API (외부용)", description = "상품의 수량을 증가시킵니다.")
    public ApiResult increaseQuantity(@PathVariable UUID productId, @RequestParam int quantity) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        try {
            ProductResponseDto responseDto = productService.increaseQuantity(productId, quantity);
            apiResult.set(ApiResultError.NO_ERROR).setResultData(responseDto);
        } catch (HubException e) {
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }
        return apiResult;
    }


    @PatchMapping("/external/decrease/{productId}")
    @Operation(summary = "상품 수량 감소 API (외부용)", description = "상품의 수량을 감소시킵니다.")
    public ApiResult externalDecreaseQuantity(@PathVariable UUID productId, @RequestParam int quantity) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        try {
            ProductResponseDto responseDto = productService.decreaseQuantity(productId, quantity);
            apiResult.set(ApiResultError.NO_ERROR).setResultData(responseDto);
        } catch (HubException e) {
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }
        return apiResult;
    }


}
