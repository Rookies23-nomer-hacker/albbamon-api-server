package com.api.global.error;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum GlobalErrorCode implements ErrorCode {
    /**
     * 400 Bad Request
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    INVALID_ENUM_CODE(HttpStatus.BAD_REQUEST, "잘못된 Enum class code 입니다."),
    NOT_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "리프레시 토큰이 아닙니다"),
    MISMATCH_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    /**
     * 401 Unauthorized
     */
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "리소스 접근 권한이 없습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다"),
    ZOOM_CREATE_TOKEN_ERROR(HttpStatus.UNAUTHORIZED, "zoom Token 생성에 실패했습니다."),

    /**
     * 403 Forbidden
     */
    FORBIDDEN(HttpStatus.FORBIDDEN, "리소스 접근 권한이 없습니다."),

    /**
     * 404 Not Found
     */
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "엔티티를 찾을 수 없습니다."),

    /**
     * 405 Method Not Allowed
     */
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "잘못된 HTTP method 요청입니다."),

    /**
     * 409 Conflict
     */
    CONFLICT(HttpStatus.CONFLICT, "이미 존재하는 리소스입니다."),

    /**
     * 500 Internal Server Error
     */
    ZOOM_CREATE_INTERNAL_SERVER(HttpStatus.INTERNAL_SERVER_ERROR, "zoom 미팅방 생성에 실패했습니다."),
    TOSS_ERROR_FORMAT(HttpStatus.INTERNAL_SERVER_ERROR, "Toss payments error 형식 변환이 잘 못되었습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
