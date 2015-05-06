package com.an.core.exception;

import org.springframework.validation.Errors;

public class BadRequestException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public BadRequestException() {
        super();
    }

    public BadRequestException(String msg) {
        super(msg);
    }

    public BadRequestException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public BadRequestException(Throwable cause) {
        super(cause);
    }

    public BadRequestException(Errors errors) {
        super(errors.getAllErrors().get(0).getDefaultMessage());
    }


}