package com.roni.library.sdk;

import java.io.Serializable;

/**
 * Standardized result wrapper for all NativeRuntimeSDK operations.
 * Enforces zero unhandled runtime exceptions.
 *
 * @param <T> Payload data type
 */
public final class NativeRuntimeResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private final boolean success;
    private final T data;
    private final int errorCode;
    private final String errorMessage;
    private final long timestamp;

    private NativeRuntimeResult(boolean success, T data, int errorCode, String errorMessage) {
        this.success = success;
        this.data = data;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.timestamp = System.currentTimeMillis();
    }

    public static <T> NativeRuntimeResult<T> success(T data) {
        return new NativeRuntimeResult<>(true, data, 0, null);
    }

    public static <T> NativeRuntimeResult<T> failure(int errorCode, String errorMessage) {
        return new NativeRuntimeResult<>(false, null, errorCode, errorMessage);
    }

    public static <T> NativeRuntimeResult<T> failure(Throwable t) {
        String msg = t != null ? t.getMessage() : "Unknown exception";
        return new NativeRuntimeResult<>(false, null, 500, msg);
    }

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        if (success) {
            return "NativeRuntimeResult.Success{data=" + data + ", ts=" + timestamp + "}";
        } else {
            return "NativeRuntimeResult.Failure{code=" + errorCode + ", message='" + errorMessage + "'}";
        }
    }
}
