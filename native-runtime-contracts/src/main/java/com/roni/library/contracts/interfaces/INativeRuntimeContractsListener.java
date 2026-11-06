package com.roni.library.contracts.interfaces;

import androidx.annotation.NonNull;
import com.roni.library.contracts.common.RuntimeState;

/**
 * Listener interface for NativeRuntimeContracts events and lifecycle changes.
 *
 * @author infoadmstudio2020-cyber
 */
public interface INativeRuntimeContractsListener {

    /**
     * Called when the library is successfully initialized.
     */
    void onInitialized();

    /**
     * Called when an error occurs during runtime execution.
     *
     * @param errorCode numeric error code
     * @param message   human-readable message
     */
    void onError(int errorCode, @NonNull String message);

    /**
     * Default callback triggered on state transitions.
     *
     * @param oldState previous state
     * @param newState transitioned state
     */
    default void onStateChanged(@NonNull RuntimeState oldState, @NonNull RuntimeState newState) {}
}

