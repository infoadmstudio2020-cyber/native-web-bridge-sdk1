package com.roni.library.platform;

import java.io.Serializable;

/**
 * Standard generic result container for NativePlatformEngine operations.
 *
 * @param <T> Payload data type
 * @author infoadmstudio2020-cyber
 */
public class PlatformEngineResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private final boolean success;
    private final T data;
    private final String errorMessage;
    private final int errorCode;

    private PlatformEngineResult(boolean success, T data, String errorMessage, int errorCode) {
        this.success = success;
        this.data = data;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public static <T> PlatformEngineResult<T> success(T data) {
        return new PlatformEngineResult<>(true, data, null, 0);
    }

    public static <T> PlatformEngineResult<T> error(int errorCode, String errorMessage) {
        return new PlatformEngineResult<>(false, null, errorMessage, errorCode);
    }

    public static <T> PlatformEngineResult<T> error(String errorMessage) {
        return new PlatformEngineResult<>(false, null, errorMessage, -1);
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

    @Override
    public String toString() {
        return "PlatformEngineResult{" +
                "success=" + success +
                ", data=" + data +
                ", errorMessage='" + errorMessage + '\'' +
                ", errorCode=" + errorCode +
                '}';
    }
}
