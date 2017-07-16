package io.emccarthy.clientservice.function.exception;

public class FunctionServiceResponseException extends Exception {

    public FunctionServiceResponseException(String message) {
        super(message);
    }

    public FunctionServiceResponseException(String message, Throwable cause) {
        super(message, cause);
    }
}
