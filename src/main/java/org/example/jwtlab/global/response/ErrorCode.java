package org.example.jwtlab.global.response;

public interface ErrorCode {
    int getStatus();
    String getCode();
    String getMessage();
}