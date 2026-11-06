package com.roni.library.ui;

import java.io.Serializable;

/**
 * Result model alias adhering to PROMPTFORNativeRuntimeUI.txt specifications.
 *
 * @param <T> data type
 */
public class NativeUIHostResult<T> implements Serializable {

    private final boolean success;
    private final T data;
    private final String errorMessage;
    private final int errorCode;

    private NativeUIHostResult(boolean success, T data, String errorMessage, int errorCode) {
        this.success = success;
        this.data = data;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public static <T> NativeUIHostResult<T> success(T data) {
        return new NativeUIHostResult<>(true, data, null, 0);
    }

    public static <T> NativeUIHostResult<T> failure(String errorMessage, int errorCode) {
        return new NativeUIHostResult<>(false, null, errorMessage, errorCode);
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
