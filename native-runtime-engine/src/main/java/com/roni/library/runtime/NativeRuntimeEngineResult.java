package com.roni.library.runtime;

import java.io.Serializable;

/**
 * Standardized result wrapper for all NativeRuntimeEngine operations.
 *
 * @param <T> data type
 */
public class NativeRuntimeEngineResult<T> implements Serializable {

    private final boolean success;
    private final T data;
    private final String errorMessage;
    private final int errorCode;

    private NativeRuntimeEngineResult(boolean success, T data, String errorMessage, int errorCode) {
        this.success = success;
        this.data = data;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public static <T> NativeRuntimeEngineResult<T> success(T data) {
        return new NativeRuntimeEngineResult<>(true, data, null, 0);
    }

    public static <T> NativeRuntimeEngineResult<T> failure(String errorMessage, int errorCode) {
        return new NativeRuntimeEngineResult<>(false, null, errorMessage, errorCode);
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
