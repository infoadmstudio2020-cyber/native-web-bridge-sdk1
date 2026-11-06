package com.roni.library.bridge.handler;

import com.roni.library.bridge.WebBridge;
import com.roni.library.contracts.api.IApiCallback;
import com.roni.library.contracts.api.IApiHandler;
import com.roni.library.contracts.api.NativeApiError;
import com.roni.library.contracts.api.NativeApiRequest;
import com.roni.library.contracts.api.NativeApiResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Router command handler that handles requests dispatched targeting bridge subsystems.
 */
public final class BridgeCommandHandler implements IApiHandler {

    private final WebBridge webBridge;

    public BridgeCommandHandler(WebBridge webBridge) {
        this.webBridge = webBridge;
    }

    public String getRoutePrefix() {
        return "bridge";
    }

    @Override
    public String getPath() {
        return "/bridge";
    }

    @Override
    public String getAction() {
        return "dispatch";
    }

    @Override
    public NativeApiResponse handleRequest(NativeApiRequest request) {
        if (request == null) {
            return NativeApiResponse.error("unknown", new NativeApiError("400", "Null request"));
        }
        Map<String, Object> data = new HashMap<>();
        data.put("status", "handled");
        data.put("action", request.getAction());
        data.put("requestId", request.getRequestId());
        data.put("receivedParameters", request.getParameters());
        return NativeApiResponse.success(request.getRequestId(), data);
    }

    public void handle(NativeApiRequest request, IApiCallback callback) {
        if (callback == null) {
            return;
        }
        if (request == null) {
            callback.onError(new NativeApiError("400", "Null request"));
            return;
        }
        callback.onResponse(handleRequest(request));
    }
}
