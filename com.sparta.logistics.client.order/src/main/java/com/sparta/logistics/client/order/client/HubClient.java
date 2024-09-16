package com.sparta.logistics.client.order.client;

import com.sparta.logistics.common.model.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "hub-service")
public interface HubClient {

    // ----- Hub -----
    /**
     * 허브 상세 조회
     * @param hubId
     * @return
     */
    @GetMapping("/hubs/{hubId}")
    ApiResult getHubById(@PathVariable("hubId") UUID hubId);


    // ----- Hub Path -----

    /**
     * 허브경로 목록
     * @param departureHubId
     * @param arrivalHubId
     * @return
     */
    @GetMapping("/hub-paths/hubList")
    ApiResult getHubPaths(@RequestParam UUID departureHubId, @RequestParam UUID arrivalHubId);


    // ----- product -----
    /**
     * 상품 정보
     * @param productId
     * @return
     */
    @GetMapping("/products/{productId}")
    ApiResult getProduct(@PathVariable("productId") UUID productId);

    /**
     * 상품 수량 업데이트(감소)
     * @param productId
     * @param quantity
     * @return
     */
    @PatchMapping("/external/decrease/{productId}")
    ApiResult externalDecreaseQuantity(@PathVariable UUID productId, @RequestParam int quantity);

    /**
     * 상품 수량 업데이트(증가)
     * @param productId
     * @param quantity
     * @return
     */
    @PatchMapping("/external/increase/{productId}")
    public ApiResult increaseQuantity(@PathVariable UUID productId, @RequestParam int quantity);


}

