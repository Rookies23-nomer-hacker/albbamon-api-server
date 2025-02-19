package com.api.global.error.exception;

import com.api.global.error.ErrorCode;
import com.api.global.error.GlobalErrorCode;

public class MethodNotAllowedException extends BusinessException {
    public MethodNotAllowedException() {
        super(GlobalErrorCode.METHOD_NOT_ALLOWED);
    }

    public MethodNotAllowedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
