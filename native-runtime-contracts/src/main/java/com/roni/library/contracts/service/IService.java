package com.roni.library.contracts.service;

import androidx.annotation.NonNull;

/**
 * Universal lifecycle contract for managed services across NativeRuntime layers.
 *
 * @author infoadmstudio2020-cyber
 */
public interface IService {

    /**
     * Unique identifier or human-readable service name.
     *
     * @return service name
     */
    @NonNull
    String getServiceName();

    /**
     * Called when the service is started by the service manager.
     */
    void onStart();

    /**
     * Called when the service is stopped and resources must be cleaned up.
     */
    void onStop();

    /**
     * Check whether this service is currently active and running.
     *
     * @return true if running
     */
    boolean isRunning();
}
