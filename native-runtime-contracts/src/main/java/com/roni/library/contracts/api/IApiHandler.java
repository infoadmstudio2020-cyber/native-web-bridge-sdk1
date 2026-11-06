package com.roni.library.contracts.api;

import androidx.annotation.NonNull;

/**
 * Universal contract for API command handlers in NativeRuntime ecosystem.
 * Handlers process RPC requests routed from WebBridge or Native callers.
 *
 * @author infoadmstudio2020-cyber
 */
public interface IApiHandler {

    /**
     * Get the URI path or domain namespace handled by this handler (e.g. "platform", "storage").
     *
     * @return path string
     */
    @NonNull
    String getPath();

    /**
     * Get the action supported by this handler, or "*" for all actions under this path.
     *
     * @return action string
     */
    @NonNull
    String getAction();

    /**
     * Execute the request and return a structured response.
     *
     * @param request the incoming API request
     * @return response object
     */
    @NonNull
    NativeApiResponse handleRequest(@NonNull NativeApiRequest request);
}
