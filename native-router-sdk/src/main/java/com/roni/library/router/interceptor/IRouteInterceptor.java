package com.roni.library.router.interceptor;

import com.roni.library.contracts.api.IApiCallback;
import com.roni.library.contracts.api.NativeApiError;
import com.roni.library.contracts.api.NativeApiRequest;
import com.roni.library.contracts.api.NativeApiResponse;
import com.roni.library.router.RouteMatchResult;

/**
 * Interceptor interface defining chain-of-responsibility lifecycle hooks
 * during route matching, invocation, and response dispatch.
 *
 * @author infoadmstudio2020-cyber
 */
public interface IRouteInterceptor {

    /**
     * Executed before the route handler is invoked.
     *
     * @param request     The incoming request
     * @param matchResult The route match result containing handler and extracted variables
     * @param callback    The response callback (used to abort dispatch with an error)
     * @return true to continue pipeline execution; false to halt pipeline
     */
    boolean preHandle(NativeApiRequest request, RouteMatchResult matchResult, IApiCallback callback);

    /**
     * Executed after the route handler produces a successful response.
     *
     * @param request  The original request
     * @param response The response produced by the handler
     */
    void postHandle(NativeApiRequest request, NativeApiResponse response);

    /**
     * Executed when an error occurs during route matching or handler execution.
     *
     * @param request The original request
     * @param error   The error that occurred
     */
    void onError(NativeApiRequest request, NativeApiError error);
}
