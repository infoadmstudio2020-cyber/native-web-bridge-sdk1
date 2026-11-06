package com.roni.library.platform.handler;

import com.roni.library.contracts.api.IApiCallback;
import com.roni.library.contracts.api.IApiHandler;
import com.roni.library.contracts.api.NativeApiError;
import com.roni.library.contracts.api.NativeApiRequest;
import com.roni.library.contracts.api.NativeApiResponse;
import com.roni.library.platform.PlatformEngine;

import java.util.HashMap;
import java.util.Map;

/**
 * Universal RPC command handler dispatching platform hardware and OS bridge actions.
 *
 * @author infoadmstudio2020-cyber
 */
public class PlatformCommandHandler implements IApiHandler {

    private static final String ROUTE_PREFIX = "/platform";
    private static final String ACTION_ALL = "*";

    public String getRoutePrefix() {
        return ROUTE_PREFIX;
    }

    @Override
    public String getPath() {
        return ROUTE_PREFIX;
    }

    @Override
    public String getAction() {
        return ACTION_ALL;
    }

    @Override
    public NativeApiResponse handleRequest(NativeApiRequest request) {
        String reqId = request != null ? request.getRequestId() : "req_platform_unknown";
        if (request == null) {
            return NativeApiResponse.error(reqId, 400, new NativeApiError("400", "Request cannot be null"));
        }

        String action = request.getAction();
        if (action == null) {
            action = "";
        }

        PlatformEngine engine = PlatformEngine.getInstance();
        Map<String, Object> data = new HashMap<>();

        try {
            switch (action.toLowerCase()) {
                case "device.info":
                case "device":
                    data.put("deviceInfo", engine.getDeviceService().getDeviceInfo().toMap());
                    break;

                case "battery.status":
                case "battery":
                    data.put("batteryStatus", engine.getBatteryService().getBatteryStatus().toMap());
                    break;

                case "network.status":
                case "network":
                    data.put("networkStatus", engine.getNetworkService().getNetworkStatus());
                    break;

                case "storage.stats":
                case "storage":
                    data.put("storageStats", engine.getStorageService().getStorageStats());
                    break;

                case "clipboard.get":
                    data.put("clipboardText", engine.getClipboardService().getText());
                    data.put("hasText", engine.getClipboardService().hasText());
                    break;

                case "clipboard.set":
                    Object textObj = request.getParameter("text");
                    String text = textObj != null ? textObj.toString() : "";
                    boolean setSuccess = engine.getClipboardService().setText(text);
                    data.put("success", setSuccess);
                    data.put("text", text);
                    break;

                case "toast.show":
                    Object msgObj = request.getParameter("message");
                    String msg = msgObj != null ? msgObj.toString() : "Notification";
                    Object longObj = request.getParameter("long");
                    boolean isLong = longObj instanceof Boolean && (Boolean) longObj;
                    engine.getToastService().showToast(msg, isLong);
                    data.put("shown", true);
                    data.put("message", msg);
                    break;

                case "toast.vibrate":
                case "vibrate":
                    Object durationObj = request.getParameter("duration");
                    long duration = 100;
                    if (durationObj instanceof Number) {
                        duration = ((Number) durationObj).longValue();
                    }
                    engine.getToastService().vibrate(duration);
                    data.put("vibrated", true);
                    data.put("durationMs", duration);
                    break;

                case "diagnostic":
                case "health":
                default:
                    data.put("device", engine.getDeviceService().getDeviceInfo().toMap());
                    data.put("battery", engine.getBatteryService().getBatteryStatus().toMap());
                    data.put("network", engine.getNetworkService().getNetworkStatus());
                    data.put("storage", engine.getStorageService().getStorageStats());
                    data.put("state", engine.getState().name());
                    data.put("initialized", engine.isInitialized());
                    break;
            }

            return NativeApiResponse.success(reqId, data);
        } catch (Throwable t) {
            NativeApiError error = new NativeApiError("PLATFORM_HANDLER_ERROR", t.getMessage(), t.toString());
            return NativeApiResponse.error(reqId, 500, error);
        }
    }

    public void handle(NativeApiRequest request, IApiCallback callback) {
        NativeApiResponse response = handleRequest(request);
        if (callback != null) {
            callback.onResponse(response);
        }
    }
}
