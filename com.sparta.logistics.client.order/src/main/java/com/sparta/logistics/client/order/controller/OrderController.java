package com.sparta.logistics.client.order.controller;

import com.sparta.logistics.client.order.common.exception.OrderProcException;
import com.sparta.logistics.client.order.common.type.OrderStatus;
import com.sparta.logistics.client.order.dto.*;
import com.sparta.logistics.client.order.model.validation.OrderValid0001;
import com.sparta.logistics.client.order.model.validation.OrderValid0002;
import com.sparta.logistics.client.order.service.OrderProcService;
import com.sparta.logistics.client.order.service.OrderService;
import com.sparta.logistics.client.order.model.Order;
import com.sparta.logistics.common.controller.CustomApiController;
import com.sparta.logistics.common.model.ApiResult;
import com.sparta.logistics.common.type.ApiResultError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Order 요청 API", description = "Order 요청 내역 관리 목적의 API Docs")
public class OrderController extends CustomApiController {

    private final OrderService orderService;

    private final OrderProcService orderProcService;

    /**
     * 주문 생성
     * @param orderRequestDto
     * @return
     */
    @Operation(summary = "주문 생성", description = "주문 생성")
    @PostMapping
    public ApiResult createOrder(@RequestBody @Validated({OrderValid0001.class}) OrderRequestDto orderRequestDto,
                                 Errors errors){
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        if (errors.hasErrors()) {
            return bindError(errors, apiResult);
        }

        try {
            OrderResponseDto order = orderProcService.createOrder(orderRequestDto);
            apiResult.set(ApiResultError.NO_ERROR).setResultData(order);

        } catch(OrderProcException e) {
            apiResult.set(e.getCode());
        }

        return apiResult;
    }

    /**
     * 주문 취소
     * @param orderId
     * @return
     */
    @Operation(summary = "주문 삭제", description = "주문 삭제")
    @DeleteMapping("/{id}")
    public ApiResult deleteOrder(@PathVariable("id") UUID orderId,
                                 @RequestHeader("X-User-Id") String userId) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);

        log.info("====> loginUser {}", userId);
        try {
            orderProcService.deleteOrder(orderId, OrderStatus.ORDER_CANCELED, userId);
            apiResult.set(ApiResultError.NO_ERROR).setResultMessage("삭제되었습니다.");
        } catch (OrderProcException e) {
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }

        return apiResult;
    }

    /**
     * 주문 자동 취소
     * @param productId
     * @return
     */
    @Operation(summary = "주문 삭제", description = "주문 삭제")
    @DeleteMapping("/cancel/{productId}")
    public ApiResult cancelOrder(@PathVariable("productId") UUID productId,
                                 @RequestHeader("X-User-Id") String userId) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);

        try {
            orderProcService.cancelOrder(productId, userId);
            apiResult.set(ApiResultError.NO_ERROR).setResultMessage("삭제되었습니다.");
        } catch (OrderProcException e) {
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }

        return apiResult;
    }

    /**
     * 주문 목록 조회(삭제건 제외)
     * @param orderSearchCriteria
     * @return
     */

    @Operation(summary = "주문 목록 조회", description = "주문 목록 조회")
    @GetMapping("/list")
    public ApiResult getAllOrders(OrderSearchCriteria orderSearchCriteria,
                                  @PageableDefault Pageable pageable) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);

        Page<OrderResponseDto> orderList = orderService.searchOrders(orderSearchCriteria, pageable);
        apiResult.set(ApiResultError.NO_ERROR).setList(orderList).setPageInfo(orderList);

        return apiResult;
    }

    /**
     * 주문 상세 조회(주문 품목 포함)
     * @param orderId
     * @return
     */

    @Operation(summary = "주문 상세 조회", description = "주문 상세 조회")
    @GetMapping("/{id}")
    public ApiResult getOrder(@PathVariable("id") UUID orderId) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);

        try {
        //OrderResponseDto order = orderService.getOrderWithOrderProducts(orderId);
        OrderResponseDto order = orderProcService.getOrderDetail(orderId);
        apiResult.set(ApiResultError.NO_ERROR).setResultData(order);

        } catch (OrderProcException e) {
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }

        return apiResult;
    }

    /**
     * 주문수정
     * @param orderRequestDto
     * @return
     */
    @Operation(summary = "주문 수정", description = "주문 수정")
    @PatchMapping
    public ApiResult updateOrder(@RequestBody @Validated({OrderValid0002.class}) OrderRequestDto orderRequestDto,
                                 @RequestHeader("X-User-Id") String userId) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        log.info("login User {}", userId);
        try {
            OrderResponseDto order = orderProcService.updateOrder(orderRequestDto, userId);
            apiResult.set(ApiResultError.NO_ERROR).setResultData(order);
        } catch(OrderProcException e) {
            apiResult.set(e.getCode());
        }

        return apiResult;
    }


    /**
     * 주문 상세 정보를 AI를 이용하여 요약
      * @param orderId
     * @return
     */
    @Operation(summary = "주문 상세 ai", description = "주문 상세 ai 요약")
    @GetMapping("/ai/{orderId}")
    public ApiResult getOrderDetailWithAi(@PathVariable UUID orderId) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);

        try {
            OrderAIResponseDto retOrder = orderProcService.getOrderAI(orderId);
            apiResult.set(ApiResultError.NO_ERROR).setResultData(retOrder);
        } catch (OrderProcException e) {
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }
        return apiResult;
    }


}
