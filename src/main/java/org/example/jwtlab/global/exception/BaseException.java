package org.example.jwtlab.global.exception;

import lombok.Getter;
import org.example.jwtlab.global.response.ErrorCode;

@Getter
public class BaseException extends RuntimeException {
    private final ErrorCode errorCode;

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
