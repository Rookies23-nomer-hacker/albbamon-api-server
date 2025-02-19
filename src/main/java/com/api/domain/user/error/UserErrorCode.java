package com.api.domain.user.error;

import com.api.global.error.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum UserErrorCode implements ErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    USER_CONFLICT(HttpStatus.CONFLICT, "이미 가입된 사용자입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
