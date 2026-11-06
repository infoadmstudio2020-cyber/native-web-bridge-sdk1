package com.roni.library.theme.handler;

import com.roni.library.contracts.api.IApiCallback;
import com.roni.library.contracts.api.IApiHandler;
import com.roni.library.contracts.api.NativeApiError;
import com.roni.library.contracts.api.NativeApiRequest;
import com.roni.library.contracts.api.NativeApiResponse;
import com.roni.library.contracts.theme.IThemeConfig;
import com.roni.library.theme.WebThemeBridge;
import com.roni.library.theme.manager.ThemeManager;
import com.roni.library.theme.model.ThemeConfig;
import com.roni.library.theme.model.ThemeConfig.ThemeMode;
import com.roni.library.theme.parser.ThemeParser;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles incoming API requests for the "theme" route prefix.
 */
public class ThemeCommandHandler implements IApiHandler {

    private final ThemeManager themeManager;

    public ThemeCommandHandler(ThemeManager themeManager) {
        this.themeManager = themeManager != null ? themeManager : new ThemeManager();
    }

    @Override
    public String getPath() {
        return "theme";
    }

    @Override
    public String getAction() {
        return "*";
    }

    public String getRoutePrefix() {
        return "theme";
    }

    public ThemeManager getThemeManager() {
        return themeManager;
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
                case "getTheme": {
                    IThemeConfig active = themeManager.getActiveTheme();
                    Map<String, Object> data = new HashMap<>();
                    data.put("themeName", active.getThemeName());
                    data.put("darkMode", active.isDarkMode());
                    data.put("primaryColor", active.getPrimaryColor());
                    data.put("surfaceColor", active.getSurfaceColor());
                    data.put("textColor", active.getTextColor());

                    if (active instanceof ThemeConfig) {
                        ThemeConfig tc = (ThemeConfig) active;
                        data.put("themeMode", tc.getThemeMode().name());
                        data.put("secondaryColor", tc.getSecondaryColorHex());
                        data.put("backgroundColor", tc.getBackgroundColorHex());
                        data.put("statusBarColor", tc.getStatusBarColorHex());
                        data.put("navigationBarColor", tc.getNavigationBarColorHex());
                    }
                    return NativeApiResponse.success(reqId, data);
                }
                case "setThemeMode": {
                    String modeStr = request.getStringParam("mode", "SYSTEM");
                    ThemeMode mode;
                    try {
                        mode = ThemeMode.valueOf(modeStr.toUpperCase());
                    } catch (Throwable t) {
                        mode = ThemeMode.SYSTEM;
                    }
                    themeManager.setThemeMode(mode);
                    Map<String, Object> data = new HashMap<>();
                    data.put("mode", mode.name());
                    return NativeApiResponse.success(reqId, data);
                }
                case "applyTheme": {
                    Map<String, Object> params = request.getParameters();
                    ThemeConfig config = ThemeParser.parse(params);
                    themeManager.applyTheme(config);
                    Map<String, Object> data = new HashMap<>();
                    data.put("applied", true);
                    return NativeApiResponse.success(reqId, data);
                }
                case "isSystemDark": {
                    boolean dark = themeManager.getSystemThemeObserver().isSystemDark();
                    Map<String, Object> data = new HashMap<>();
                    data.put("isSystemDark", dark);
                    return NativeApiResponse.success(reqId, data);
                }
                case "setSystemDark": {
                    boolean dark = request.getBooleanParam("dark", false);
                    themeManager.getSystemThemeObserver().setSystemDark(dark);
                    Map<String, Object> data = new HashMap<>();
                    data.put("systemDark", dark);
                    return NativeApiResponse.success(reqId, data);
                }
                case "getCssVariables": {
                    IThemeConfig active = themeManager.getActiveTheme();
                    String css = WebThemeBridge.generateCssVariables(active);
                    Map<String, Object> data = new HashMap<>();
                    data.put("css", css);
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

