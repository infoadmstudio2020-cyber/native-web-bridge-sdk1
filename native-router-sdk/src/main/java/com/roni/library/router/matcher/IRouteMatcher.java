package com.roni.library.router.matcher;

import com.roni.library.contracts.api.IApiHandler;
import com.roni.library.router.RouteMatchResult;

/**
 * Interface defining contract for matching request URIs against route patterns.
 *
 * @author infoadmstudio2020-cyber
 */
public interface IRouteMatcher {

    /**
     * Matches a pattern against a request URI.
     *
     * @param pattern    The pattern string (e.g. "app://user/{id}" or "/api/echo")
     * @param requestUri The actual request URI to evaluate
     * @param handler    The candidate handler registered for this pattern
     * @return RouteMatchResult containing matched status, path variables, and query parameters
     */
    RouteMatchResult match(String pattern, String requestUri, IApiHandler handler);
}
