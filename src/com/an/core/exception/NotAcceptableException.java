package com.an.core.exception;

public class NotAcceptableException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public NotAcceptableException() {
        super();
    }

    public NotAcceptableException(String msg) {
        super(msg);
    }

    public NotAcceptableException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public NotAcceptableException(Throwable cause) {
        super(cause);
    }
}