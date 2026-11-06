package com.roni.library.devtools;

/**
 * Type alias for NativeDevToolsSDKResult complying with DevToolsSDK conventions.
 */
public class DevToolsSDKResult<T> extends NativeDevToolsSDKResult<T> {

    public DevToolsSDKResult(boolean success, T data, String errorMessage, int errorCode) {
        super(success, data, errorMessage, errorCode);
    }

    public static <T> DevToolsSDKResult<T> success(T data) {
        return new DevToolsSDKResult<>(true, data, null, 0);
    }

    public static <T> DevToolsSDKResult<T> failure(String errorMessage, int errorCode) {
        return new DevToolsSDKResult<>(false, null, errorMessage, errorCode);
    }

    public static <T> DevToolsSDKResult<T> failure(String errorMessage) {
        return new DevToolsSDKResult<>(false, null, errorMessage, -1);
    }
}
