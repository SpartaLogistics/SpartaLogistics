package com.sparta.logistics.client.order.common.exception;

import com.sparta.logistics.common.type.ApiResultError;

public class OrderException extends Exception {

    private static final long serialVersionUID = 2287905851925635164L;

    private final ApiResultError code;

    public OrderException(ApiResultError code) {
        super();
        this.code = code;
    }

    public OrderException(String message, Throwable cause, ApiResultError code) {
        super(message, cause);
        this.code = code;
    }

    public OrderException(String message, ApiResultError code) {
        super(message);
        this.code = code;
    }

    public OrderException(Throwable cause, ApiResultError code) {
        super(cause);
        this.code = code;
    }

    public ApiResultError getCode() {
        return this.code;
    }

}