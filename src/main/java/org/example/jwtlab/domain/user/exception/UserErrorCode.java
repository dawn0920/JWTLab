package org.example.jwtlab.domain.user.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.jwtlab.global.response.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements ErrorCode {
    USER_ID_NOT_FOUND(HttpStatus.NOT_FOUND.value() ,"U001","존재하지 않는 사용자 입니다."),
    INVALID_USER_EMAIL(HttpStatus.UNAUTHORIZED.value() ,"U002","유효하지 않은 사용자 이메일입니다."),
    INVALID_USER_PASSWORD(HttpStatus.UNAUTHORIZED.value() ,"U003","유효하지 않은 사용자 비밀번호입니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN.value(), "U004", "접근 권한이 없습니다.")
    ;


    private final int status;
    private final String code;
    private final String message;
}
