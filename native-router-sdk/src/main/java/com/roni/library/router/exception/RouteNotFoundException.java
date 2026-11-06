package com.roni.library.router.exception;

/**
 * Exception thrown when no route handler is matched for a given URI.
 *
 * @author infoadmstudio2020-cyber
 */
public class RouteNotFoundException extends Exception {
    private final String requestUri;

    public RouteNotFoundException(String requestUri) {
        super("Route not found for URI: " + requestUri);
        this.requestUri = requestUri;
    }

    public RouteNotFoundException(String requestUri, String message) {
        super(message);
        this.requestUri = requestUri;
    }

    public String getRequestUri() {
        return requestUri;
    }
}
