package com.roni.library.theme;

import java.io.Serializable;

/**
 * Standard result model for NativeThemeEngine operations.
 *
 * @param <T> Payload data type
 */
public class NativeThemeEngineResult<T> implements Serializable {

    private final boolean success;
    private final T data;
    private final String errorMessage;
    private final int errorCode;

    public NativeThemeEngineResult(boolean success, T data, String errorMessage, int errorCode) {
        this.success = success;
        this.data = data;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public static <T> NativeThemeEngineResult<T> success(T data) {
        return new NativeThemeEngineResult<>(true, data, null, 0);
    }

    public static <T> NativeThemeEngineResult<T> failure(String errorMessage, int errorCode) {
        return new NativeThemeEngineResult<>(false, null, errorMessage, errorCode);
    }

    public static <T> NativeThemeEngineResult<T> failure(String errorMessage) {
        return new NativeThemeEngineResult<>(false, null, errorMessage, -1);
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
        return "NativeThemeEngineResult{" +
                "success=" + success +
                ", data=" + data +
                ", errorMessage='" + errorMessage + '\'' +
                ", errorCode=" + errorCode +
                '}';
    }
}
