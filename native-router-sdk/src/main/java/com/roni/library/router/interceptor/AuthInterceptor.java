package com.roni.library.router.interceptor;

import com.roni.library.contracts.api.IApiCallback;
import com.roni.library.contracts.api.NativeApiError;
import com.roni.library.contracts.api.NativeApiRequest;
import com.roni.library.contracts.api.NativeApiResponse;
import com.roni.library.router.RouteMatchResult;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Security interceptor that guards routes against unauthorized access.
 * Protected route prefixes require a valid authentication token.
 *
 * @author infoadmstudio2020-cyber
 */
public class AuthInterceptor implements IRouteInterceptor {

    private final Set<String> protectedPrefixes = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private volatile String validToken;

    public AuthInterceptor() {
    }

    public AuthInterceptor(String validToken) {
        this.validToken = validToken;
    }

    /**
     * Adds a URL path or URI prefix that requires authentication.
     *
     * @param prefix The route prefix to protect (e.g. "/admin", "app://secure")
     * @return This interceptor for chaining
     */
    public AuthInterceptor addProtectedPrefix(String prefix) {
        if (prefix != null && !prefix.trim().isEmpty()) {
            protectedPrefixes.add(prefix.trim());
        }
        return this;
    }

    /**
     * Sets or updates the valid authentication token.
     *
     * @param token Valid token string
     */
    public void setValidToken(String token) {
        this.validToken = token;
    }

    public String getValidToken() {
        return validToken;
    }

    @Override
    public boolean preHandle(NativeApiRequest request, RouteMatchResult matchResult, IApiCallback callback) {
        if (request == null) {
            return false;
        }

        String path = request.getPath();
        if (path == null) {
            path = "";
        }

        boolean isProtected = false;
        for (String prefix : protectedPrefixes) {
            if (path.startsWith(prefix)) {
                isProtected = true;
                break;
            }
        }

        if (!isProtected) {
            return true;
        }

        // Validate token from parameters
        Object tokenObj = request.getParameter("token");
        if (tokenObj == null) {
            tokenObj = request.getParameter("authToken");
        }
        if (tokenObj == null) {
            tokenObj = request.getParameter("Authorization");
        }

        String providedToken = tokenObj != null ? tokenObj.toString() : null;

        if (validToken != null && validToken.equals(providedToken)) {
            return true;
        }

        // Authorization failed
        NativeApiError authError = new NativeApiError(
                "AUTH_401",
                "Unauthorized: Access to route '" + path + "' requires a valid authentication token.",
                "Missing or invalid token"
        );

        if (callback != null) {
            callback.onError(authError);
        }
        return false;
    }

    @Override
    public void postHandle(NativeApiRequest request, NativeApiResponse response) {
        // No-op for AuthInterceptor post-handle
    }

    @Override
    public void onError(NativeApiRequest request, NativeApiError error) {
        // No-op for AuthInterceptor on-error
    }
}
