package com.roni.library.runtime;

import com.roni.library.runtime.source.WebSource;
import com.roni.library.runtime.source.WebSourceResolver;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Intercepts outgoing web resource requests for offline caching, CSP, and origin validation.
 */
public class WebResourceInterceptor {

    private final WebSourceResolver resolver;
    private final List<String> allowedOrigins = new CopyOnWriteArrayList<>();
    private volatile boolean strictOriginValidation = true;

    public WebResourceInterceptor(WebSourceResolver resolver) {
        this.resolver = resolver != null ? resolver : new WebSourceResolver();
        this.allowedOrigins.add("http://127.0.0.1");
        this.allowedOrigins.add("http://localhost");
        this.allowedOrigins.add("https://domain.local");
    }

    public void addAllowedOrigin(String origin) {
        if (origin != null && !allowedOrigins.contains(origin)) {
            allowedOrigins.add(origin);
        }
    }

    public boolean isAllowed(String url) {
        if (!strictOriginValidation || url == null) {
            return true;
        }
        for (String origin : allowedOrigins) {
            if (url.startsWith(origin)) {
                return true;
            }
        }
        return false;
    }

    public WebSource intercept(String url) {
        if (!isAllowed(url)) {
            return WebSource.notFound(url);
        }
        return resolver.resolve(url);
    }

    public List<String> getAllowedOrigins() {
        return new ArrayList<>(allowedOrigins);
    }

    public void setStrictOriginValidation(boolean strictOriginValidation) {
        this.strictOriginValidation = strictOriginValidation;
    }

    public WebSourceResolver getResolver() {
        return resolver;
    }
}
