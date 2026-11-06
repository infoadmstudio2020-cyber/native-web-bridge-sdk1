package com.roni.library.contracts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Base exception for NativeRuntimeContracts.
 *
 * @author infoadmstudio2020-cyber
 */
public class NativeRuntimeContractsException extends RuntimeException {

    private final int code;

    /** Create exception with message. */
    public NativeRuntimeContractsException(@NonNull String message) {
        super(message);
        this.code = -1;
    }

    /** Create exception with message and cause. */
    public NativeRuntimeContractsException(@NonNull String message, @Nullable Throwable cause) {
        super(message, cause);
        this.code = -1;
    }

    /** Create exception with error code. */
    public NativeRuntimeContractsException(@NonNull String message, int code) {
        super(message);
        this.code = code;
    }

    /** @return numeric error code, or -1 if not set */
    public int getCode() { return code; }
}
