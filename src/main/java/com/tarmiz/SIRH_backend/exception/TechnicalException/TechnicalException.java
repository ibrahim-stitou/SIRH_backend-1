package com.tarmiz.SIRH_backend.exception.TechnicalException;

public class TechnicalException extends RuntimeException {
    private final int httpStatus;

    public TechnicalException(String message, int httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public TechnicalException(String message, int httpStatus, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}