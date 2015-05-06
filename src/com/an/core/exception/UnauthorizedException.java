package com.an.core.exception;

public class UnauthorizedException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public UnauthorizedException() {
        super();
    }

    public UnauthorizedException(String msg) {
        super(msg);
    }

    public UnauthorizedException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UnauthorizedException(Throwable cause) {
        super(cause);
    }
}