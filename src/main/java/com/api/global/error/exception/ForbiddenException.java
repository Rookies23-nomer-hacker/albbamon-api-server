package com.api.global.error.exception;

import com.api.global.error.ErrorCode;
import com.api.global.error.GlobalErrorCode;

public class ForbiddenException extends BusinessException {
    public ForbiddenException() {
        super(GlobalErrorCode.FORBIDDEN);
    }

    public ForbiddenException(ErrorCode errorCode) {
        super(errorCode);
    }
}

