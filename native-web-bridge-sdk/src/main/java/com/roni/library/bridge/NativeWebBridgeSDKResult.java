package com.roni.library.bridge;

/**
 * Standardized result wrapper for SDK initialization and execution operations.
 *
 * @param <T> data type
 */
public final class NativeWebBridgeSDKResult<T> {

    private final boolean success;
    private final T data;
    private final String errorMessage;
    private final int errorCode;

    public NativeWebBridgeSDKResult(boolean success, T data, String errorMessage, int errorCode) {
        this.success = success;
        this.data = data;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public static <T> NativeWebBridgeSDKResult<T> success(T data) {
        return new NativeWebBridgeSDKResult<>(true, data, null, 0);
    }

    public static <T> NativeWebBridgeSDKResult<T> success() {
        return new NativeWebBridgeSDKResult<>(true, null, null, 0);
    }

    public static <T> NativeWebBridgeSDKResult<T> error(String message, int code) {
        return new NativeWebBridgeSDKResult<>(false, null, message, code);
    }

    public static <T> NativeWebBridgeSDKResult<T> error(String message) {
        return new NativeWebBridgeSDKResult<>(false, null, message, -1);
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
        return "NativeWebBridgeSDKResult{" +
                "success=" + success +
                ", data=" + data +
                ", errorMessage='" + errorMessage + '\'' +
                ", errorCode=" + errorCode +
                '}';
    }
}
