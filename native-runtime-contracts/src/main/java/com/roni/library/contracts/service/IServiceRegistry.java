package com.roni.library.contracts.service;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Universal service registry contract for decoupled dependency injection.
 *
 * @author infoadmstudio2020-cyber
 */
public interface IServiceRegistry {

    /**
     * Register a singleton service implementation.
     *
     * @param serviceClass interface class
     * @param instance     concrete service instance
     * @param <T>          type constraint
     */
    <T extends IService> void registerService(@NonNull Class<T> serviceClass, @NonNull T instance);

    /**
     * Retrieve a registered service instance.
     *
     * @param serviceClass interface class
     * @param <T>          type constraint
     * @return registered instance, or null if not registered
     */
    @Nullable
    <T extends IService> T getService(@NonNull Class<T> serviceClass);

    /**
     * Unregister a service instance.
     *
     * @param serviceClass interface class
     */
    void unregisterService(@NonNull Class<? extends IService> serviceClass);

    /**
     * Check if a service of the given class is currently registered.
     *
     * @param serviceClass interface class
     * @return true if present
     */
    boolean hasService(@NonNull Class<? extends IService> serviceClass);
}
