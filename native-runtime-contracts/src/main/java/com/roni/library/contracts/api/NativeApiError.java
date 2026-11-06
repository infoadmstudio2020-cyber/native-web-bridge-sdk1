package com.roni.library.contracts.api;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.Serializable;

/**
 * Standardized error structure for NativeRuntime API calls.
 *
 * @author infoadmstudio2020-cyber
 */
public final class NativeApiError implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String code;
    private final String message;
    private final String details;

    public NativeApiError(@NonNull String code, @NonNull String message, @Nullable String details) {
        this.code = code != null ? code : "UNKNOWN_ERROR";
        this.message = message != null ? message : "An unknown error occurred";
        this.details = details != null ? details : "";
    }

    public NativeApiError(@NonNull String code, @NonNull String message) {
        this(code, message, null);
    }

    @NonNull
    public String getCode() {
        return code;
    }

    @NonNull
    public String getMessage() {
        return message;
    }

    @NonNull
    public String getDetails() {
        return details;
    }

    @NonNull
    @Override
    public String toString() {
        return "NativeApiError{code='" + code + "', message='" + message + "', details='" + details + "'}";
    }
}
