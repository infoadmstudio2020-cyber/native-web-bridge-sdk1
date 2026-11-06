package com.roni.library.runtime.source;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Resolves URI requests to local package assets or mock endpoints.
 */
public class WebSourceResolver {

    private final Map<String, WebSource> routeRegistry = new ConcurrentHashMap<>();

    public WebSourceResolver() {
        // Register default landing page
        registerRoute("/", WebSource.ofText("/", "<!DOCTYPE html><html><head><title>NativeRuntime</title></head><body><h1>NativeRuntime Container</h1></body></html>", "text/html"));
        registerRoute("/index.html", WebSource.ofText("/index.html", "<!DOCTYPE html><html><head><title>NativeRuntime</title></head><body><h1>NativeRuntime Container</h1></body></html>", "text/html"));
    }

    public void registerRoute(String path, WebSource source) {
        if (path != null && source != null) {
            routeRegistry.put(path, source);
        }
    }

    public void unregisterRoute(String path) {
        if (path != null) {
            routeRegistry.remove(path);
        }
    }

    public WebSource resolve(String url) {
        if (url == null || url.trim().isEmpty()) {
            return WebSource.notFound("");
        }

        String path = url;
        if (url.startsWith("http://") || url.startsWith("https://")) {
            int slashIndex = url.indexOf('/', url.indexOf("://") + 3);
            if (slashIndex >= 0) {
                path = url.substring(slashIndex);
            } else {
                path = "/";
            }
        }

        // Strip query params
        int queryIdx = path.indexOf('?');
        if (queryIdx >= 0) {
            path = path.substring(0, queryIdx);
        }

        WebSource source = routeRegistry.get(path);
        if (source != null) {
            return source;
        }

        return WebSource.notFound(url);
    }

    public void clear() {
        routeRegistry.clear();
    }

    public int getRouteCount() {
        return routeRegistry.size();
    }
}
