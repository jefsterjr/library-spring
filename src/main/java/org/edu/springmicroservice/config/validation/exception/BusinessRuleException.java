package org.edu.springmicroservice.config.validation.exception;

public abstract class BusinessRuleException extends RuntimeException{
    protected BusinessRuleException() {
    }

    protected BusinessRuleException(String message) {
        super(message);
    }

    protected BusinessRuleException(String message, Throwable cause) {
        super(message, cause);
    }

    protected BusinessRuleException(Throwable cause) {
        super(cause);
    }

    protected BusinessRuleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
