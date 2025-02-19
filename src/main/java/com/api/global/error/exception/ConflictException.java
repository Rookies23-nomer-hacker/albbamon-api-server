package com.api.global.error.exception;

import com.api.global.error.ErrorCode;
import com.api.global.error.GlobalErrorCode;

public class ConflictException extends BusinessException {
    public ConflictException() {
        super(GlobalErrorCode.CONFLICT);
    }

    public ConflictException(ErrorCode errorCode) {
        super(errorCode);
    }
}


