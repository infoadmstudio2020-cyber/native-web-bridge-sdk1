package com.roni.library.router.handler;

import com.roni.library.contracts.api.IApiCallback;
import com.roni.library.contracts.api.IApiHandler;
import com.roni.library.contracts.api.NativeApiRequest;
import com.roni.library.contracts.api.NativeApiResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Built-in handler that echoes back the request parameters and action.
 *
 * @author infoadmstudio2020-cyber
 */
public class EchoCommandHandler implements IApiHandler {

    private static final String ROUTE_PREFIX = "/echo";
    private static final String ACTION_ECHO = "echo";

    public String getRoutePrefix() {
        return ROUTE_PREFIX;
    }

    @Override
    public String getPath() {
        return ROUTE_PREFIX;
    }

    @Override
    public String getAction() {
        return ACTION_ECHO;
    }

    @Override
    public NativeApiResponse handleRequest(NativeApiRequest request) {
        String reqId = request != null ? request.getRequestId() : "req_unknown";
        Map<String, Object> data = new HashMap<>();

        if (request != null) {
            data.put("echoAction", request.getAction());
            data.put("echoPath", request.getPath());
            data.put("echoTimestamp", request.getTimestamp());
            if (request.getParameters() != null) {
                data.put("echoParameters", new HashMap<>(request.getParameters()));
            }
        }
        data.put("status", "ok");

        return NativeApiResponse.success(reqId, data);
    }

    public void handle(NativeApiRequest request, IApiCallback callback) {
        NativeApiResponse response = handleRequest(request);
        if (callback != null) {
            callback.onResponse(response);
        }
    }
}
