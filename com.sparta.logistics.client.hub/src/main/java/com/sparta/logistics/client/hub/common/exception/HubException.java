package com.sparta.logistics.client.hub.common.exception;

import com.sparta.logistics.common.type.ApiResultError;
import lombok.Getter;

@Getter
public class HubException extends Exception {

    private static final long serialVersionUID = 2287905851925635164L;

    private final ApiResultError code;

    public HubException(ApiResultError code) {
        super(code.getMessage());
        this.code = code;
    }

    public HubException(String message, Throwable cause, ApiResultError code) {
        super(message, cause);
        this.code = code;
    }

    public HubException(String message, ApiResultError code) {
        super(message);
        this.code = code;
    }

    public HubException(Throwable cause, ApiResultError code) {
        super(cause);
        this.code = code;
    }

    public ApiResultError getCode() {
        return this.code;
    }

}
