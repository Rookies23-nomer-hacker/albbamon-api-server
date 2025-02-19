package com.api.domain.recruitment.error;

import com.api.global.error.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum RecruitmentErrorCode implements ErrorCode {
    RECRUITMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 채용공고입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
