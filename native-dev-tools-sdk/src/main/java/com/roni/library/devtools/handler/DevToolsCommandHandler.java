package com.roni.library.devtools.handler;

import com.roni.library.contracts.api.IApiCallback;
import com.roni.library.contracts.api.IApiHandler;
import com.roni.library.contracts.api.NativeApiError;
import com.roni.library.contracts.api.NativeApiRequest;
import com.roni.library.contracts.api.NativeApiResponse;
import com.roni.library.devtools.service.NativeDevToolsService;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles incoming API requests for the "devtools" route prefix.
 */
public class DevToolsCommandHandler implements IApiHandler {

    private final NativeDevToolsService service;

    public DevToolsCommandHandler(NativeDevToolsService service) {
        this.service = service != null ? service : new NativeDevToolsService();
    }

    @Override
    public String getPath() {
        return "devtools";
    }

    @Override
    public String getAction() {
        return "*";
    }

    public String getRoutePrefix() {
        return "devtools";
    }

    public NativeDevToolsService getService() {
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
                case "getMetrics": {
                    Map<String, Object> metrics = service.getPerformanceProfiler().getMetricsSnapshot();
                    return NativeApiResponse.success(reqId, metrics);
                }
                case "getHealth": {
                    Map<String, Object> health = service.getDiagnosticsManager().collectServiceHealth();
                    return NativeApiResponse.success(reqId, health);
                }
                case "getBridgeMetrics": {
                    Map<String, Object> bm = service.getDiagnosticsManager().collectBridgeMetrics();
                    return NativeApiResponse.success(reqId, bm);
                }
                case "getManifest": {
                    Map<String, Object> mf = service.getDiagnosticsManager().generatePlatformManifest();
                    return NativeApiResponse.success(reqId, mf);
                }
                case "exportHar": {
                    Map<String, Object> har = service.getNetworkTrafficInspector().exportHar();
                    return NativeApiResponse.success(reqId, har);
                }
                case "clearTraffic": {
                    service.getNetworkTrafficInspector().clear();
                    Map<String, Object> data = new HashMap<>();
                    data.put("cleared", true);
                    return NativeApiResponse.success(reqId, data);
                }
                case "startProfiling": {
                    service.getPerformanceProfiler().startRecording();
                    Map<String, Object> data = new HashMap<>();
                    data.put("recording", true);
                    return NativeApiResponse.success(reqId, data);
                }
                case "stopProfiling": {
                    service.getPerformanceProfiler().stopRecording();
                    Map<String, Object> data = new HashMap<>();
                    data.put("recording", false);
                    return NativeApiResponse.success(reqId, data);
                }
                case "dispatchCdp": {
                    int id = 1;
                    String method = request.getStringParam("method", "Runtime.enable");
                    Map<String, Object> params = request.getParameters();
                    Map<String, Object> cdpRes = service.getDevToolsServer().dispatchCdpMessage(id, method, params);
                    return NativeApiResponse.success(reqId, cdpRes);
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
