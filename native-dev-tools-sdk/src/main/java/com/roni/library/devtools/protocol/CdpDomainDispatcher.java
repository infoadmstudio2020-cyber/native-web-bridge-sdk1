package com.roni.library.devtools.protocol;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Dispatches incoming Chrome DevTools Protocol (CDP) commands to registered domain handlers.
 */
public class CdpDomainDispatcher {

    public interface DomainHandler {
        Map<String, Object> handle(String method, Map<String, Object> params);
    }

    private final Map<String, DomainHandler> domains = new ConcurrentHashMap<>();

    public CdpDomainDispatcher() {
        registerBuiltInDomains();
    }

    public void registerDomain(String domain, DomainHandler handler) {
        if (domain != null && handler != null) {
            domains.put(domain, handler);
        }
    }

    public Map<String, Object> dispatch(int id, String method, Map<String, Object> params) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", id);

        if (method == null || !method.contains(".")) {
            Map<String, Object> err = new HashMap<>();
            err.put("code", -32601);
            err.put("message", "Method not found: " + method);
            response.put("error", err);
            return response;
        }

        int dot = method.indexOf('.');
        String domain = method.substring(0, dot);
        String subMethod = method.substring(dot + 1);

        DomainHandler handler = domains.get(domain);
        if (handler != null) {
            try {
                Map<String, Object> result = handler.handle(subMethod, params != null ? params : new HashMap<>());
                response.put("result", result != null ? result : new HashMap<>());
            } catch (Throwable t) {
                Map<String, Object> err = new HashMap<>();
                err.put("code", -32000);
                err.put("message", t.getMessage());
                response.put("error", err);
            }
        } else {
            Map<String, Object> err = new HashMap<>();
            err.put("code", -32601);
            err.put("message", "Domain not found: " + domain);
            response.put("error", err);
        }

        return response;
    }

    private void registerBuiltInDomains() {
        // Runtime domain
        registerDomain("Runtime", new DomainHandler() {
            @Override
            public Map<String, Object> handle(String method, Map<String, Object> params) {
                Map<String, Object> res = new HashMap<>();
                if ("enable".equals(method)) {
                    res.put("enabled", true);
                } else if ("evaluate".equals(method)) {
                    Map<String, Object> val = new HashMap<>();
                    val.put("type", "string");
                    val.put("value", "NativeDevTools Evaluation Mock");
                    res.put("result", val);
                }
                return res;
            }
        });

        // Network domain
        registerDomain("Network", new DomainHandler() {
            @Override
            public Map<String, Object> handle(String method, Map<String, Object> params) {
                Map<String, Object> res = new HashMap<>();
                res.put("enabled", true);
                return res;
            }
        });

        // Page domain
        registerDomain("Page", new DomainHandler() {
            @Override
            public Map<String, Object> handle(String method, Map<String, Object> params) {
                Map<String, Object> res = new HashMap<>();
                res.put("frameTree", new HashMap<>());
                return res;
            }
        });
    }
}
