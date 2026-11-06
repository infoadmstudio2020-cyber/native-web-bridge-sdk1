package com.roni.library.security;

/**
 * Generic result container for security operations.
 *
 * @param <T> Payload type
 * @author infoadmstudio2020-cyber
 */
public final class SecurityEngineResult<T> {

    private final boolean success;
    private final T data;
    private final String errorMessage;
    private final int errorCode;

    private SecurityEngineResult(boolean success, T data, String errorMessage, int errorCode) {
        this.success = success;
        this.data = data;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public static <T> SecurityEngineResult<T> success(T data) {
        return new SecurityEngineResult<>(true, data, null, 0);
    }

    public static <T> SecurityEngineResult<T> error(int errorCode, String errorMessage) {
        return new SecurityEngineResult<>(false, null, errorMessage, errorCode);
    }

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
