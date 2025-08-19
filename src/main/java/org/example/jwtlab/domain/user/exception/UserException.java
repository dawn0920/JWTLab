package org.example.jwtlab.domain.user.exception;

import org.example.jwtlab.global.exception.BaseException;

public class UserException extends BaseException {
    public UserException(UserErrorCode errorCode) {
        super(errorCode);
    }
}
