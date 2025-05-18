package com.example.cybz_back.exception;

public class EmailProcessingException extends RuntimeException {
    public EmailProcessingException(String message) {
        super(message);
    }

    public EmailProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
