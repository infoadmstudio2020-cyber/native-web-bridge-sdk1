package com.roni.library.router;

import java.io.Serializable;

/**
 * Standard generic result container for NativeRouterSDK operations.
 *
 * @param <T> The payload data type
 * @author infoadmstudio2020-cyber
 */
public class NativeRouterSDKResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private final boolean success;
    private final T data;
    private final String errorMessage;
    private final int errorCode;

    private NativeRouterSDKResult(boolean success, T data, String errorMessage, int errorCode) {
        this.success = success;
        this.data = data;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public static <T> NativeRouterSDKResult<T> success(T data) {
        return new NativeRouterSDKResult<>(true, data, null, 0);
    }

    public static <T> NativeRouterSDKResult<T> error(int errorCode, String errorMessage) {
        return new NativeRouterSDKResult<>(false, null, errorMessage, errorCode);
    }

    public static <T> NativeRouterSDKResult<T> error(String errorMessage) {
        return new NativeRouterSDKResult<>(false, null, errorMessage, -1);
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
        return "NativeRouterSDKResult{" +
                "success=" + success +
                ", data=" + data +
                ", errorMessage='" + errorMessage + '\'' +
                ", errorCode=" + errorCode +
                '}';
    }
}
