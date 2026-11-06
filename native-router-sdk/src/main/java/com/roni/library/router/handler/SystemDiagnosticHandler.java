package com.roni.library.router.handler;

import com.roni.library.contracts.api.IApiCallback;
import com.roni.library.contracts.api.IApiHandler;
import com.roni.library.contracts.api.NativeApiRequest;
import com.roni.library.contracts.api.NativeApiResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Built-in handler providing system diagnostic telemetry.
 *
 * @author infoadmstudio2020-cyber
 */
public class SystemDiagnosticHandler implements IApiHandler {

    private static final String ROUTE_PREFIX = "/system/diagnostic";
    private static final String ACTION_DIAGNOSTIC = "diagnostic";

    public String getRoutePrefix() {
        return ROUTE_PREFIX;
    }

    @Override
    public String getPath() {
        return ROUTE_PREFIX;
    }

    @Override
    public String getAction() {
        return ACTION_DIAGNOSTIC;
    }

    @Override
    public NativeApiResponse handleRequest(NativeApiRequest request) {
        String reqId = request != null ? request.getRequestId() : "req_unknown";
        Map<String, Object> data = new HashMap<>();

        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long maxMemory = runtime.maxMemory();

        data.put("availableProcessors", runtime.availableProcessors());
        data.put("totalMemoryBytes", totalMemory);
        data.put("freeMemoryBytes", freeMemory);
        data.put("usedMemoryBytes", totalMemory - freeMemory);
        data.put("maxMemoryBytes", maxMemory);
        data.put("activeThreadCount", Thread.activeCount());
        data.put("timestamp", System.currentTimeMillis());
        data.put("javaVmVersion", System.getProperty("java.vm.version", "unknown"));
        data.put("osName", System.getProperty("os.name", "unknown"));
        data.put("healthStatus", "HEALTHY");

        return NativeApiResponse.success(reqId, data);
    }

    public void handle(NativeApiRequest request, IApiCallback callback) {
        NativeApiResponse response = handleRequest(request);
        if (callback != null) {
            callback.onResponse(response);
        }
    }
}
