package com.api.domain.apply.error;

import com.api.global.error.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum ApplyErrorCode implements ErrorCode {
    APPLY_NOT_FOUND(HttpStatus.NOT_FOUND, "지원내역이 존재하지 않습니다."),
    APPLY_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 지원한 내역이 존재합니다");

    private final HttpStatus httpStatus;
    private final String message;
}
