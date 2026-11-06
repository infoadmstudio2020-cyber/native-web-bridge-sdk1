package com.roni.library.ui.theme;

import com.roni.library.contracts.theme.IThemeConfig;
import com.roni.library.theme.ThemeEngine;
import java.util.HashMap;
import java.util.Map;

/**
 * Bridges NativeRuntimeUI widgets with native-theme-engine's active color tokens.
 */
public class UIThemeBridge {

    public static Map<String, Object> getActiveThemeColors() {
        Map<String, Object> colors = new HashMap<>();
        try {
            if (ThemeEngine.isInitialized()) {
                IThemeConfig config = ThemeEngine.getActiveTheme();
                if (config != null) {
                    colors.put("themeName", config.getThemeName());
                    colors.put("primaryColor", config.getPrimaryColor());
                    colors.put("surfaceColor", config.getSurfaceColor());
                    colors.put("textColor", config.getTextColor());
                    colors.put("isDark", config.isDarkMode());
                    return colors;
                }
            }
        } catch (Throwable ignored) {}

        // Fallback Material Design theme tokens
        colors.put("themeName", "Dark");
        colors.put("primaryColor", "#6200EE");
        colors.put("surfaceColor", "#1E1E1E");
        colors.put("textColor", "#FFFFFF");
        colors.put("isDark", true);
        return colors;
    }
}



