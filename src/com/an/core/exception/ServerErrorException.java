package com.an.core.exception;

public class ServerErrorException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ServerErrorException() {
        super();
    }

    public ServerErrorException(String msg) {
        super(msg);
    }

    public ServerErrorException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ServerErrorException(Throwable cause) {
        super(cause);
    }
}