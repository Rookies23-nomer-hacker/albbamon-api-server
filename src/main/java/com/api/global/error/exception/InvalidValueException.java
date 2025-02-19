package com.api.global.error.exception;

import com.api.global.error.ErrorCode;
import com.api.global.error.GlobalErrorCode;

public class InvalidValueException extends BusinessException {
    public InvalidValueException() {
        super(GlobalErrorCode.BAD_REQUEST);
    }

    public InvalidValueException(ErrorCode errorCode) {
        super(errorCode);
    }
}
