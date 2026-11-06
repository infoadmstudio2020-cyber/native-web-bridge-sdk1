package com.roni.library.devtools;

import java.io.Serializable;

/**
 * Standard result wrapper for NativeDevToolsSDK operations.
 *
 * @param <T> Payload data type
 */
public class NativeDevToolsSDKResult<T> implements Serializable {

    private final boolean success;
    private final T data;
    private final String errorMessage;
    private final int errorCode;

    public NativeDevToolsSDKResult(boolean success, T data, String errorMessage, int errorCode) {
        this.success = success;
        this.data = data;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public static <T> NativeDevToolsSDKResult<T> success(T data) {
        return new NativeDevToolsSDKResult<>(true, data, null, 0);
    }

    public static <T> NativeDevToolsSDKResult<T> failure(String errorMessage, int errorCode) {
        return new NativeDevToolsSDKResult<>(false, null, errorMessage, errorCode);
    }

    public static <T> NativeDevToolsSDKResult<T> failure(String errorMessage) {
        return new NativeDevToolsSDKResult<>(false, null, errorMessage, -1);
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
        return "NativeDevToolsSDKResult{" +
                "success=" + success +
                ", data=" + data +
                ", errorMessage='" + errorMessage + '\'' +
                ", errorCode=" + errorCode +
                '}';
    }
}
