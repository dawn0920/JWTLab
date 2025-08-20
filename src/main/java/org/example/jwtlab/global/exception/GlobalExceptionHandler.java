package org.example.jwtlab.global.exception;

import org.example.jwtlab.global.response.BaseErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseErrorResponse> handleBizError(BaseException exception) {
        return ResponseEntity
                .status(exception.getErrorCode().getStatus())
                .body(new BaseErrorResponse(exception.getErrorCode()));
    }

}