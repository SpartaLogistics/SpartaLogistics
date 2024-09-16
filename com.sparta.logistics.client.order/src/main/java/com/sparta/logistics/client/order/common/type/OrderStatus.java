package com.sparta.logistics.client.order.common.type;

public enum OrderStatus {
    ORDER_PENDING,          // 주문대기
    ORDER_COMPLETED,        // 주문완료
    ORDER_CANCELED,    // 주문취소 -> 사용자 주문취소
    ORDER_AUTO_CANCELED,   // 주문취소 -> 상품 삭제처리로 인한 취소
    ORDER_FAILED            // 주문실패 -> error ? or
}
