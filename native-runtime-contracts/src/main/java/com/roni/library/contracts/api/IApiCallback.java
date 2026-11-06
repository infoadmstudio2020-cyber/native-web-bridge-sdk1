package com.roni.library.contracts.api;

import androidx.annotation.NonNull;

/**
 * Universal asynchronous callback interface for NativeRuntime API operations.
 *
 * @author infoadmstudio2020-cyber
 */
public interface IApiCallback {

    /**
     * Called when the API request successfully completes.
     *
     * @param response the success response object
     */
    void onResponse(@NonNull NativeApiResponse response);

    /**
     * Called when the API request encounters an error.
     *
     * @param error the error descriptor
     */
    void onError(@NonNull NativeApiError error);
}
