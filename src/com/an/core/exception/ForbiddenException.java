package com.an.core.exception;

public class ForbiddenException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ForbiddenException() {
        super();
    }

    public ForbiddenException(String msg) {
        super(msg);
    }

    public ForbiddenException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ForbiddenException(Throwable cause) {
        super(cause);
    }
}