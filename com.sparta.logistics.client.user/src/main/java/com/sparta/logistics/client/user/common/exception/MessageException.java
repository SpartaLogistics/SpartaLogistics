package com.sparta.logistics.client.user.common.exception;

import com.sparta.logistics.common.type.ApiResultError;
import lombok.Getter;

@Getter
public class MessageException extends Exception {
    private static final long serialVersionUID = 2287905851925635164L;

    private final ApiResultError code;

    public MessageException(ApiResultError code) {
        super(code.getMessage());
        this.code = code;
    }

    public MessageException(String message, Throwable cause, ApiResultError code) {
        super(message, cause);
        this.code = code;
    }

    public MessageException(String message, ApiResultError code) {
        super(message);
        this.code = code;
    }

    public MessageException(Throwable cause, ApiResultError code) {
        super(cause);
        this.code = code;
    }

    public ApiResultError getCode() {
        return this.code;
    }

}
