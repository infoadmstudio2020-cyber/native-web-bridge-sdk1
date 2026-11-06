package com.roni.library.theme.parser;

import com.roni.library.theme.ColorTokenResolver;
import com.roni.library.theme.model.ThemeConfig;
import com.roni.library.theme.model.ThemeConfig.ThemeMode;
import java.util.Map;

/**
 * Parses dynamic JSON/Map theme tokens into strongly-typed ThemeConfig instances.
 */
public class ThemeParser {

    public static ThemeConfig parse(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return ThemeConfig.defaultLight();
        }

        String themeName = getString(map, "themeName", "CustomTheme");
        boolean darkMode = getBoolean(map, "darkMode", false);

        String modeStr = getString(map, "themeMode", darkMode ? "DARK" : "LIGHT");
        ThemeMode themeMode;
        try {
            themeMode = ThemeMode.valueOf(modeStr.toUpperCase());
        } catch (Throwable t) {
            themeMode = darkMode ? ThemeMode.DARK : ThemeMode.LIGHT;
        }

        int defaultPrimary = darkMode ? 0xFFBB86FC : 0xFF6200EE;
        int defaultSecondary = darkMode ? 0xFF03DAC6 : 0xFF03DAC5;
        int defaultBg = darkMode ? 0xFF121212 : 0xFFFFFFFF;
        int defaultSurface = darkMode ? 0xFF1E1E1E : 0xFFF5F5F5;
        int defaultText = darkMode ? 0xFFFFFFFF : 0xFF000000;
        int defaultStatus = darkMode ? 0xFF121212 : 0xFF6200EE;
        int defaultNav = darkMode ? 0xFF121212 : 0xFFFFFFFF;

        int primaryColor = parseColor(map.get("primaryColor"), defaultPrimary);
        int secondaryColor = parseColor(map.get("secondaryColor"), defaultSecondary);
        int backgroundColor = parseColor(map.get("backgroundColor"), defaultBg);
        int surfaceColor = parseColor(map.get("surfaceColor"), defaultSurface);
        int textColor = parseColor(map.get("textColor"), defaultText);
        int statusBarColor = parseColor(map.get("statusBarColor"), defaultStatus);
        int navigationBarColor = parseColor(map.get("navigationBarColor"), defaultNav);

        boolean statusBarIconsLight = getBoolean(map, "statusBarIconsLight", !darkMode);
        boolean navigationBarIconsLight = getBoolean(map, "navigationBarIconsLight", !darkMode);

        return new ThemeConfig(
                themeName,
                themeMode,
                darkMode,
                primaryColor,
                secondaryColor,
                backgroundColor,
                surfaceColor,
                textColor,
                statusBarColor,
                navigationBarColor,
                statusBarIconsLight,
                navigationBarIconsLight
        );
    }

    public static int parseColor(Object colorObj, int defaultColor) {
        return ColorTokenResolver.parseColor(colorObj, defaultColor);
    }

    private static String getString(Map<String, Object> map, String key, String defaultValue) {
        Object val = map.get(key);
        return val != null ? val.toString() : defaultValue;
    }

    private static boolean getBoolean(Map<String, Object> map, String key, boolean defaultValue) {
        Object val = map.get(key);
        if (val instanceof Boolean) {
            return (Boolean) val;
        }
        if (val != null) {
            return Boolean.parseBoolean(val.toString());
        }
        return defaultValue;
    }
}
