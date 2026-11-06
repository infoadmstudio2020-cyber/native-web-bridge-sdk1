package com.roni.library.ui.handler;

import com.roni.library.contracts.api.IApiCallback;
import com.roni.library.contracts.api.IApiHandler;
import com.roni.library.contracts.api.NativeApiError;
import com.roni.library.contracts.api.NativeApiRequest;
import com.roni.library.contracts.api.NativeApiResponse;
import com.roni.library.ui.CustomBottomSheet;
import com.roni.library.ui.NativeActionSheet;
import com.roni.library.ui.NativeToast;
import com.roni.library.ui.host.NativeUIHost;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles incoming API requests for the "ui" route prefix.
 */
public class NativeUICommandHandler implements IApiHandler {

    private final NativeUIHost uiHost;
    private final NativeToast toast;
    private final CustomBottomSheet bottomSheet;
    private final NativeActionSheet actionSheet;

    public NativeUICommandHandler(NativeUIHost uiHost) {
        this.uiHost = uiHost != null ? uiHost : new NativeUIHost();
        this.toast = new NativeToast();
        this.bottomSheet = new CustomBottomSheet();
        this.actionSheet = new NativeActionSheet();
    }

    @Override
    public String getPath() {
        return "ui";
    }

    @Override
    public String getAction() {
        return "*";
    }

    public String getRoutePrefix() {
        return "ui";
    }

    public NativeUIHost getUiHost() {
        return uiHost;
    }

    public NativeToast getToast() {
        return toast;
    }

    public CustomBottomSheet getBottomSheet() {
        return bottomSheet;
    }

    public NativeActionSheet getActionSheet() {
        return actionSheet;
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
                case "showAlert": {
                    String title = request.getStringParam("title", "Alert");
                    String msg = request.getStringParam("message", "");
                    uiHost.getDialogManager().showAlert(title, msg, null);
                    Map<String, Object> data = new HashMap<>();
                    data.put("shown", true);
                    return NativeApiResponse.success(reqId, data);
                }
                case "showToast": {
                    String msg = request.getStringParam("message", "");
                    toast.show(msg);
                    Map<String, Object> data = new HashMap<>();
                    data.put("shown", true);
                    return NativeApiResponse.success(reqId, data);
                }
                case "showBottomSheet": {
                    String title = request.getStringParam("title", "");
                    bottomSheet.setTitle(title);
                    bottomSheet.show();
                    Map<String, Object> data = new HashMap<>();
                    data.put("shown", true);
                    return NativeApiResponse.success(reqId, data);
                }
                case "showLoading": {
                    String text = request.getStringParam("text", "Loading...");
                    uiHost.getLoadingOverlayManager().show(text);
                    Map<String, Object> data = new HashMap<>();
                    data.put("shown", true);
                    return NativeApiResponse.success(reqId, data);
                }
                case "hideLoading": {
                    uiHost.getLoadingOverlayManager().hide();
                    Map<String, Object> data = new HashMap<>();
                    data.put("hidden", true);
                    return NativeApiResponse.success(reqId, data);
                }
                case "setToolbarTitle": {
                    String title = request.getStringParam("title", "");
                    uiHost.getToolbarManager().setTitle(title);
                    Map<String, Object> data = new HashMap<>();
                    data.put("title", title);
                    return NativeApiResponse.success(reqId, data);
                }
                case "selectTab": {
                    String tabId = request.getStringParam("tabId", "");
                    uiHost.getNavigationManager().selectTab(tabId);
                    Map<String, Object> data = new HashMap<>();
                    data.put("selectedTab", uiHost.getNavigationManager().getSelectedTab());
                    return NativeApiResponse.success(reqId, data);
                }
                                case "isComponentEnabled": {
                    String categoryName = request.getStringParam("category", "");
                    String key = request.getStringParam("key", "");
                    boolean enabled = false;
                    try {
                        com.roni.library.contracts.ui.UICategory cat = com.roni.library.contracts.ui.UICategory.valueOf(categoryName);
                        com.roni.library.contracts.ui.UIComponentType type = new com.roni.library.contracts.ui.UIComponentType(cat, key);
                        enabled = uiHost.getConfigurationManager().isComponentEnabled(type);
                    } catch (Throwable ignored) {}
                    Map<String, Object> data = new HashMap<>();
                    data.put("enabled", enabled);
                    return NativeApiResponse.success(reqId, data);
                }
                case "setComponentVisible": {
                    String categoryName = request.getStringParam("category", "");
                    String key = request.getStringParam("key", "");
                    boolean visible = Boolean.parseBoolean(request.getStringParam("visible", "true"));
                    try {
                        com.roni.library.contracts.ui.UICategory cat = com.roni.library.contracts.ui.UICategory.valueOf(categoryName);
                        com.roni.library.contracts.ui.UIComponentType type = new com.roni.library.contracts.ui.UIComponentType(cat, key);
                        uiHost.getVisibilityManager().setVisible(type, visible);
                    } catch (Throwable ignored) {}
                    Map<String, Object> data = new HashMap<>();
                    data.put("success", true);
                    return NativeApiResponse.success(reqId, data);
                }
                case "getComponentCount": {
                    Map<String, Object> data = new HashMap<>();
                    data.put("count", uiHost.getRegistry().getComponentCount());
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
