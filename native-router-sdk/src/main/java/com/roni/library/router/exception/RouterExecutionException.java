package com.roni.library.router.exception;

import com.roni.library.contracts.api.NativeApiError;

/**
 * Exception thrown when an error occurs during router execution or dispatching.
 *
 * @author infoadmstudio2020-cyber
 */
public class RouterExecutionException extends RuntimeException {
    private final NativeApiError error;

    public RouterExecutionException(String message) {
        super(message);
        this.error = new NativeApiError("ROUTER_EXECUTION_ERROR", message);
    }

    public RouterExecutionException(String message, Throwable cause) {
        super(message, cause);
        this.error = new NativeApiError("ROUTER_EXECUTION_ERROR", message, cause != null ? cause.getMessage() : null);
    }

    public RouterExecutionException(NativeApiError error) {
        super(error != null ? error.getMessage() : "Router execution error");
        this.error = error;
    }

    public NativeApiError getError() {
        return error;
    }
}
