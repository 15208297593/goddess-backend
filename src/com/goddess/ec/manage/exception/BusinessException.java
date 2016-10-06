package com.goddess.ec.manage.exception;

public class BusinessException extends BaseException {
    public BusinessException() {
        super();
    }
    public BusinessException(String message) {
        super(message);
    }
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
    public BusinessException(Throwable cause) {
        super(cause);
    }
    public BusinessException(String message, Throwable cause,
                         boolean enableSuppression,
                         boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
