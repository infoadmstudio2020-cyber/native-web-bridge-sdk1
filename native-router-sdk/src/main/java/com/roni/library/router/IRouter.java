package com.roni.library.router;

import com.roni.library.contracts.api.IApiCallback;
import com.roni.library.contracts.api.IApiHandler;
import com.roni.library.contracts.api.NativeApiRequest;
import com.roni.library.contracts.api.NativeApiResponse;
import com.roni.library.router.interceptor.IRouteInterceptor;

/**
 * Public interface defining the contract of the NativeRouter engine.
 *
 * @author infoadmstudio2020-cyber
 */
public interface IRouter {

    /**
     * Registers an API handler for a specific route URI pattern.
     *
     * @param pathPattern The pattern (e.g. "/api/user/{id}" or "app://settings")
     * @param handler     The handler to invoke when matched
     */
    void registerRoute(String pathPattern, IApiHandler handler);

    /**
     * Registers a handler using its default route prefix or path.
     *
     * @param handler The handler to register
     */
    void registerHandler(IApiHandler handler);

    /**
     * Unregisters a route pattern.
     *
     * @param pathPattern The route pattern to remove
     */
    void unregisterRoute(String pathPattern);

    /**
     * Adds an interceptor to the execution pipeline.
     *
     * @param interceptor The interceptor to add
     */
    void addInterceptor(IRouteInterceptor interceptor);

    /**
     * Removes an interceptor from the execution pipeline.
     *
     * @param interceptor The interceptor to remove
     */
    void removeInterceptor(IRouteInterceptor interceptor);

    /**
     * Matches a request URI against all registered route patterns.
     *
     * @param requestUri The URI to match
     * @return The RouteMatchResult
     */
    RouteMatchResult match(String requestUri);

    /**
     * Checks if any registered route can handle the specified URI.
     *
     * @param requestUri The URI to test
     * @return true if a handler is registered for this URI; false otherwise
     */
    boolean canHandle(String requestUri);

    /**
     * Dispatches a request asynchronously to the matched handler through the interceptor pipeline.
     *
     * @param request  The request to dispatch
     * @param callback The callback receiving the response or error
     */
    void dispatch(NativeApiRequest request, IApiCallback callback);

    /**
     * Dispatches a request synchronously.
     *
     * @param request The request to dispatch
     * @return The response produced by the handler or error response
     */
    NativeApiResponse dispatchSync(NativeApiRequest request);
}
