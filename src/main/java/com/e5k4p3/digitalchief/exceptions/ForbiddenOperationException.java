package com.e5k4p3.digitalchief.exceptions;

public class ForbiddenOperationException extends RuntimeException {
    public ForbiddenOperationException(final String message) {
        super(message);
    }
}
