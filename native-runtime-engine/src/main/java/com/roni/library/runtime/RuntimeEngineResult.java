package com.roni.library.runtime;

import java.io.Serializable;

/**
 * Alias result wrapper adhering to PROMPTFORNativeRuntimeEngine.txt specifications.
 *
 * @param <T> data type
 */
public class RuntimeEngineResult<T> implements Serializable {

    private final boolean success;
    private final T data;
    private final String errorMessage;
    private final int errorCode;

    private RuntimeEngineResult(boolean success, T data, String errorMessage, int errorCode) {
        this.success = success;
        this.data = data;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public static <T> RuntimeEngineResult<T> success(T data) {
        return new RuntimeEngineResult<>(true, data, null, 0);
    }

    public static <T> RuntimeEngineResult<T> failure(String errorMessage, int errorCode) {
        return new RuntimeEngineResult<>(false, null, errorMessage, errorCode);
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
