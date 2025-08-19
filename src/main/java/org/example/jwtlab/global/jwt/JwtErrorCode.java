package org.example.jwtlab.global.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.jwtlab.global.response.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum JwtErrorCode implements ErrorCode {
    JWT_MISSING(HttpStatus.BAD_REQUEST.value(), "J001", "JWT 토큰이 필요합니다."),
    JWT_INVALID(HttpStatus.UNAUTHORIZED.value(), "J002", "유효하지 않은 JWT 토큰입니다."),
    JWT_EXPIRED(HttpStatus.UNAUTHORIZED.value(), "J003", "만료된 JWT 토큰입니다."),
    JWT_UNAUTHORIZED(HttpStatus.FORBIDDEN.value(), "J004", "권한이 없습니다.");

    private final int status;
    private final String code;
    private final String message;
}
