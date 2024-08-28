package com.walter.demopark.exception;

public class PasswordInvalidException extends RuntimeException {

    public PasswordInvalidException(String message) {
        super(message);
    }
}
