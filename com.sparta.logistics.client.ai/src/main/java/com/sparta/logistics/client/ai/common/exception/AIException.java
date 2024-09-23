package com.sparta.logistics.client.ai.common.exception;

import com.sparta.logistics.common.type.ApiResultError;

public class AIException extends Exception{

    private static final long serialVersionUID = 2287905851925635164L;

    private final ApiResultError code;

    public AIException(ApiResultError code) {
        super();
        this.code = code;
    }

    public AIException(String message, Throwable cause, ApiResultError code) {
        super(message, cause);
        this.code = code;
    }

    public AIException(String message, ApiResultError code) {
        super(message);
        this.code = code;
    }

    public AIException(Throwable cause, ApiResultError code) {
        super(cause);
        this.code = code;
    }

    public ApiResultError getCode() {
        return this.code;
    }

}
