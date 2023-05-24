package com.stdt.auleweb.rest;

/**
 *
 * @author giuse
 */
public class CustomException extends Exception {

    public CustomException() {
    }

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }

}
