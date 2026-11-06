package com.roni.library.platform.exception;

import com.roni.library.contracts.api.NativeApiError;

/**
 * Base exception thrown by NativePlatformEngine.
 *
 * @author infoadmstudio2020-cyber
 */
public class PlatformEngineException extends Exception {
    private final NativeApiError error;

    public PlatformEngineException(String message) {
        super(message);
        this.error = new NativeApiError("PLATFORM_ENGINE_ERROR", message);
    }

    public PlatformEngineException(String message, Throwable cause) {
        super(message, cause);
        this.error = new NativeApiError("PLATFORM_ENGINE_ERROR", message, cause != null ? cause.getMessage() : null);
    }

    public PlatformEngineException(NativeApiError error) {
        super(error != null ? error.getMessage() : "Platform engine error");
        this.error = error;
    }

    public NativeApiError getError() {
        return error;
    }
}
