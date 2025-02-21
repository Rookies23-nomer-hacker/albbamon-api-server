package com.api.domain.resume.error;

import com.api.global.error.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum RecruitmentErrorCode implements ErrorCode {
    RESUME_NOT_FOUND(HttpStatus.NOT_FOUND, "이력서가 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
