package com.roni.library.router;

import com.roni.library.contracts.api.IApiHandler;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Encapsulates the outcome of evaluating a request URI against registered routing patterns.
 *
 * @author infoadmstudio2020-cyber
 */
public class RouteMatchResult {
    private final boolean matched;
    private final String matchedPattern;
    private final IApiHandler handler;
    private final Map<String, String> pathVariables;
    private final Map<String, String> queryParams;

    private static final RouteMatchResult NOT_FOUND = new RouteMatchResult(
            false,
            null,
            null,
            Collections.emptyMap(),
            Collections.emptyMap()
    );

    public RouteMatchResult(
            boolean matched,
            String matchedPattern,
            IApiHandler handler,
            Map<String, String> pathVariables,
            Map<String, String> queryParams) {
        this.matched = matched;
        this.matchedPattern = matchedPattern;
        this.handler = handler;
        this.pathVariables = pathVariables != null ? Collections.unmodifiableMap(new HashMap<>(pathVariables)) : Collections.emptyMap();
        this.queryParams = queryParams != null ? Collections.unmodifiableMap(new HashMap<>(queryParams)) : Collections.emptyMap();
    }

    public static RouteMatchResult notFound() {
        return NOT_FOUND;
    }

    public static RouteMatchResult success(
            String matchedPattern,
            IApiHandler handler,
            Map<String, String> pathVariables,
            Map<String, String> queryParams) {
        return new RouteMatchResult(true, matchedPattern, handler, pathVariables, queryParams);
    }

    public boolean isMatched() {
        return matched;
    }

    public String getMatchedPattern() {
        return matchedPattern;
    }

    public IApiHandler getHandler() {
        return handler;
    }

    public Map<String, String> getPathVariables() {
        return pathVariables;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public String getPathVariable(String name) {
        return pathVariables.get(name);
    }

    public String getQueryParam(String name) {
        return queryParams.get(name);
    }

    @Override
    public String toString() {
        return "RouteMatchResult{" +
                "matched=" + matched +
                ", matchedPattern='" + matchedPattern + '\'' +
                ", handler=" + (handler != null ? handler.getClass().getSimpleName() : "null") +
                ", pathVariables=" + pathVariables +
                ", queryParams=" + queryParams +
                '}';
    }
}
