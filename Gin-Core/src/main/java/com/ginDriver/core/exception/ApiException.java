package com.ginDriver.core.exception;

/**
 * @author DueGin
 */
public class ApiException extends RuntimeException{
    public ApiException(String message) {
        super(message);
    }
}
