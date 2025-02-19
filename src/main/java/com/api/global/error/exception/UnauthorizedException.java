package com.api.global.error.exception;

import com.api.global.error.ErrorCode;
import com.api.global.error.GlobalErrorCode;

public class UnauthorizedException extends BusinessException {
    public UnauthorizedException() {
        super(GlobalErrorCode.UNAUTHORIZED);
    }

    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }
}

