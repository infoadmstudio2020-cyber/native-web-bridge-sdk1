package com.roni.library.runtime.handler;

import com.roni.library.contracts.api.IApiCallback;
import com.roni.library.contracts.api.IApiHandler;
import com.roni.library.contracts.api.NativeApiError;
import com.roni.library.contracts.api.NativeApiRequest;
import com.roni.library.contracts.api.NativeApiResponse;
import com.roni.library.runtime.service.NativeRuntimeService;
import com.roni.library.runtime.source.WebSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles incoming API requests for the "runtime" route prefix.
 */
public class RuntimeCommandHandler implements IApiHandler {

    private final NativeRuntimeService service;

    public RuntimeCommandHandler(NativeRuntimeService service) {
        this.service = service != null ? service : new NativeRuntimeService();
    }

    @Override
    public String getPath() {
        return "runtime";
    }

    @Override
    public String getAction() {
        return "*";
    }

    public String getRoutePrefix() {
        return "runtime";
    }

    public NativeRuntimeService getService() {
        return service;
    }

    @Override
    public NativeApiResponse handleRequest(NativeApiRequest request) {
        if (request == null) {
            return NativeApiResponse.error("unknown", new NativeApiError("INVALID_REQUEST", "Request is null"));
        }

        String action = request.getAction();
        String reqId = request.getRequestId();
        if (action == null || action.trim().isEmpty()) {
            return NativeApiResponse.error(reqId, new NativeApiError("MISSING_ACTION", "Action cannot be empty"));
        }

        try {
            switch (action) {
                case "loadUrl": {
                    String url = request.getStringParam("url", "about:blank");
                    service.getDefaultContainer().loadUrl(url);
                    Map<String, Object> data = new HashMap<>();
                    data.put("currentUrl", service.getDefaultContainer().getCurrentUrl());
                    return NativeApiResponse.success(reqId, data);
                }
                case "getVitals": {
                    Map<String, Object> vitals = service.getVitalsCollector().getMetrics();
                    return NativeApiResponse.success(reqId, vitals);
                }
                case "resolveSource": {
                    String url = request.getStringParam("url", "/");
                    WebSource source = service.getWebSourceResolver().resolve(url);
                    Map<String, Object> data = new HashMap<>();
                    data.put("url", source.getUrl());
                    data.put("statusCode", source.getStatusCode());
                    data.put("mimeType", source.getMimeType());
                    data.put("isFound", source.isFound());
                    return NativeApiResponse.success(reqId, data);
                }
                case "setCookie": {
                    String domain = request.getStringParam("domain", "127.0.0.1");
                    String cookie = request.getStringParam("cookie", "");
                    service.getCookieSyncManager().setCookie(domain, cookie);
                    Map<String, Object> data = new HashMap<>();
                    data.put("stored", true);
                    return NativeApiResponse.success(reqId, data);
                }
                case "getCookies": {
                    String domain = request.getStringParam("domain", "127.0.0.1");
                    List<String> cookies = service.getCookieSyncManager().getCookies(domain);
                    Map<String, Object> data = new HashMap<>();
                    data.put("cookies", cookies);
                    return NativeApiResponse.success(reqId, data);
                }
                case "clearCookies": {
                    service.getCookieSyncManager().clearAllCookies();
                    Map<String, Object> data = new HashMap<>();
                    data.put("cleared", true);
                    return NativeApiResponse.success(reqId, data);
                }
                default:
                    return NativeApiResponse.error(reqId, new NativeApiError("UNKNOWN_ACTION", "Action not supported: " + action));
            }
        } catch (Throwable t) {
            return NativeApiResponse.error(reqId, new NativeApiError("INTERNAL_ERROR", t.getMessage()));
        }
    }

    public void handle(NativeApiRequest request, IApiCallback callback) {
        NativeApiResponse response = handleRequest(request);
        if (callback != null) {
            if (response.isSuccess()) {
                callback.onResponse(response);
            } else {
                callback.onError(response.getError());
            }
        }
    }
}
