package com.roni.library.contracts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Universal result wrapper for NativeRuntime operations.
 * Also accessible via {@link com.roni.library.contracts.models.NativeRuntimeContractsResult}.
 *
 * @param <T> data type
 * @author infoadmstudio2020-cyber
 */
public final class NativeRuntimeContractsResult<T> {

    private final boolean success;
    private final T data;
    private final String errorMessage;
    private final int errorCode;

    private NativeRuntimeContractsResult(boolean success, @Nullable T data, @Nullable String errorMessage, int errorCode) {
        this.success = success;
        this.data = data;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    /** Create a success result with data payload. */
    @NonNull
    public static <T> NativeRuntimeContractsResult<T> success(@Nullable T data) {
        return new NativeRuntimeContractsResult<>(true, data, null, 0);
    }

    /** Create a success result with null data. */
    @NonNull
    public static <T> NativeRuntimeContractsResult<T> success() {
        return new NativeRuntimeContractsResult<>(true, null, null, 0);
    }

    /** Create an error result with message and numeric code. */
    @NonNull
    public static <T> NativeRuntimeContractsResult<T> error(@NonNull String message, int code) {
        return new NativeRuntimeContractsResult<>(false, null, message, code);
    }

    /** Create an error result with message and default error code -1. */
    @NonNull
    public static <T> NativeRuntimeContractsResult<T> error(@NonNull String message) {
        return new NativeRuntimeContractsResult<>(false, null, message, -1);
    }

    /** @return true if the operation succeeded */
    public boolean isSuccess() {
        return success;
    }

    /** @return the result data, or null on error */
    @Nullable
    public T getData() {
        return data;
    }

    /** @return error message, or null on success */
    @Nullable
    public String getErrorMessage() {
        return errorMessage;
    }

    /** @return numeric error code */
    public int getErrorCode() {
        return errorCode;
    }

    @NonNull
    @Override
    public String toString() {
        return success ? "NativeRuntimeContractsResult{success=true, data=" + data + "}"
                       : "NativeRuntimeContractsResult{success=false, code=" + errorCode + ", message='" + errorMessage + "'}";
    }
}
