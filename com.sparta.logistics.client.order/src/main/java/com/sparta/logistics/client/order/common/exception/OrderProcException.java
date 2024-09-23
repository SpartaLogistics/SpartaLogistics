package com.sparta.logistics.client.order.common.exception;

import com.sparta.logistics.common.type.ApiResultError;

public class OrderProcException extends Exception {

    private static final long serialVersionUID = 2287905851925635164L;

    private final ApiResultError code;

    public OrderProcException(ApiResultError code) {
        super(code.getMessage());
        this.code = code;
    }

    public OrderProcException(String message, Throwable cause, ApiResultError code) {
        super(message, cause);
        this.code = code;
    }

    public OrderProcException(String message, ApiResultError code) {
        super(message);
        this.code = code;
    }

    public OrderProcException(Throwable cause, ApiResultError code) {
        super(cause);
        this.code = code;
    }

    public ApiResultError getCode() {
        return this.code;
    }

}