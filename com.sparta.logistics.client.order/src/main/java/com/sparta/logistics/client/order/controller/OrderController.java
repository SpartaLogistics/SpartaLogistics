package com.sparta.logistics.client.order.controller;

import com.sparta.logistics.client.order.common.exception.OrderProcException;
import com.sparta.logistics.client.order.dto.OrderRequestDto;
import com.sparta.logistics.client.order.dto.OrderResponseDto;
import com.sparta.logistics.client.order.dto.OrderSearchDto;
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
    public ApiResult createOrder(@RequestBody @Validated OrderRequestDto orderRequestDto,
                                 Errors errors){
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        if (errors.hasErrors()) {
            return bindError(errors, apiResult);
        }

        try {
            orderProcService.createOrder(orderRequestDto);
            apiResult.set(ApiResultError.NO_ERROR);

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
    public ApiResult deleteOrder(@PathVariable("id") UUID orderId) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);

        // TODO orderProcService 로 교체
        try {
            orderProcService.deleteOrder(orderId);
            apiResult.set(ApiResultError.NO_ERROR).setResultMessage("삭제되었습니다.");
        } catch (OrderProcException e) {
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }

        return apiResult;
    }

    /**
     * 주문 목록 조회(삭제건 제외)
     * @param orderSearchDto
     * @return
     */

    @Operation(summary = "주문 목록 조회", description = "주문 목록 조회")
    @GetMapping
    public ApiResult getAllOrders(OrderSearchDto orderSearchDto) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        // 임시
        Pageable pageable = PageRequest.of(0, 20);

        Page<Order> orderList = orderService.getOrderList(orderSearchDto, pageable);
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
        OrderResponseDto order = orderService.getOrderWithOrderProducts(orderId);

        if(null != order) {
            apiResult.set(ApiResultError.NO_ERROR).setResultData(order);
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
    public ApiResult updateOrder(@RequestBody OrderRequestDto orderRequestDto) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);

        try {
            OrderResponseDto order = orderService.updateOrder(orderRequestDto);
            apiResult.set(ApiResultError.NO_ERROR).setResultData(order);
        } catch(OrderProcException e) {
            apiResult.set(e.getCode());
        }

        return apiResult;
    }


}
