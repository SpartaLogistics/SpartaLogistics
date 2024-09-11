package com.sparta.logistics.client.order.controller;

import com.sparta.logistics.client.order.common.exception.OrderException;
import com.sparta.logistics.client.order.dto.OrderRequestDto;
import com.sparta.logistics.client.order.dto.OrderResponseDto;
import com.sparta.logistics.client.order.dto.OrderSearchDto;
import com.sparta.logistics.client.order.service.OrderService;
import com.sparta.logistics.client.order.model.Order;
import com.sparta.logistics.common.model.ApiResult;
import com.sparta.logistics.common.type.ApiResultError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * 주문 생성
     * @param orderRequestDto
     * @return
     */
    @PostMapping
    public ApiResult createOrder(@RequestBody OrderRequestDto orderRequestDto) throws OrderException {
        // TODO @RequestBody binding Error 처리, validation

        log.info("Create order: {}", orderRequestDto);
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);
        // TODO orderProcService로 교체
        try {
            Order retOrder = orderService.createOrder(orderRequestDto);
            apiResult.set(ApiResultError.NO_ERROR).setResultData(orderRequestDto);

        } catch(OrderException e) {
            apiResult.set(e.getCode());
        }

        return apiResult;
    }

    /**
     * 주문 삭제
     * @param orderId
     * @return
     */
    @DeleteMapping("/{id}")
    public ApiResult deleteOrder(@PathVariable("id") UUID orderId) {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);

        // TODO orderProcService 로 교체
        try {
            orderService.deleteOrder(orderId);
            apiResult.set(ApiResultError.NO_ERROR).setResultMessage("삭제되었습니다.");
        } catch (OrderException e) {
            apiResult.set(e.getCode()).setResultMessage(e.getMessage());
        }

        return apiResult;
    }

    /**
     * 주문 목록 조회(삭제건 제외)
     * @param orderSearchDto
     * @return
     */
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
     * 주문 수정
     * @param orderRequestDto
     * @return
     * @throws OrderException
     */
    @PatchMapping
    public ApiResult updateOrder(@RequestBody OrderRequestDto orderRequestDto) throws OrderException {
        ApiResult apiResult = new ApiResult(ApiResultError.ERROR_DEFAULT);

        try {
            OrderResponseDto order = orderService.updateOrder(orderRequestDto);
            apiResult.set(ApiResultError.NO_ERROR).setResultData(order);
        } catch(OrderException e) {
            apiResult.set(e.getCode());
        }

        return apiResult;
    }
}
