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
    USER_CONFLICT(HttpStatus.CONFLICT, "이미 가입된 사용자입니다."),
    PASSWORD_INCORRECT(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    SIGN_IN_REQUIRED(HttpStatus.UNAUTHORIZED, "로그인이 필요한 서비스입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
