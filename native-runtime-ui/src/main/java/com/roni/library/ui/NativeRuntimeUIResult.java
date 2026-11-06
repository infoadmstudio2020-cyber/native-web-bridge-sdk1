package com.roni.library.ui;

import java.io.Serializable;

/**
 * Standardized result wrapper for all NativeRuntimeUI operations.
 *
 * @param <T> data type
 */
public class NativeRuntimeUIResult<T> implements Serializable {

    private final boolean success;
    private final T data;
    private final String errorMessage;
    private final int errorCode;

    private NativeRuntimeUIResult(boolean success, T data, String errorMessage, int errorCode) {
        this.success = success;
        this.data = data;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public static <T> NativeRuntimeUIResult<T> success(T data) {
        return new NativeRuntimeUIResult<>(true, data, null, 0);
    }

    public static <T> NativeRuntimeUIResult<T> failure(String errorMessage, int errorCode) {
        return new NativeRuntimeUIResult<>(false, null, errorMessage, errorCode);
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
