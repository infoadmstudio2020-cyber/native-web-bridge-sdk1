package com.roni.library.theme;

import com.roni.library.contracts.theme.IThemeConfig;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a theme color palette with comprehensive semantic design tokens.
 */
public class ThemePalette implements IThemeConfig, Serializable {

    private final String themeName;
    private final boolean darkMode;
    private final String primaryColor;
    private final String secondaryColor;
    private final String backgroundColor;
    private final String surfaceColor;
    private final String textColor;
    private final String textSecondaryColor;
    private final String accentColor;
    private final String statusBarColor;
    private final String navigationBarColor;
    private final boolean statusBarIconsLight;
    private final boolean navigationBarIconsLight;
    private final Map<String, String> customTokens;

    public ThemePalette(String themeName,
                        boolean darkMode,
                        String primaryColor,
                        String secondaryColor,
                        String backgroundColor,
                        String surfaceColor,
                        String textColor,
                        String textSecondaryColor,
                        String accentColor,
                        String statusBarColor,
                        String navigationBarColor,
                        boolean statusBarIconsLight,
                        boolean navigationBarIconsLight,
                        Map<String, String> customTokens) {
        this.themeName = themeName != null ? themeName : (darkMode ? "Dark" : "Light");
        this.darkMode = darkMode;
        this.primaryColor = primaryColor != null ? primaryColor : (darkMode ? "#BB86FC" : "#6200EE");
        this.secondaryColor = secondaryColor != null ? secondaryColor : (darkMode ? "#03DAC6" : "#03DAC5");
        this.backgroundColor = backgroundColor != null ? backgroundColor : (darkMode ? "#121212" : "#FFFFFF");
        this.surfaceColor = surfaceColor != null ? surfaceColor : (darkMode ? "#1E1E1E" : "#F5F5F5");
        this.textColor = textColor != null ? textColor : (darkMode ? "#FFFFFF" : "#000000");
        this.textSecondaryColor = textSecondaryColor != null ? textSecondaryColor : (darkMode ? "#B0B0B0" : "#666666");
        this.accentColor = accentColor != null ? accentColor : "#FF4081";
        this.statusBarColor = statusBarColor != null ? statusBarColor : (darkMode ? "#121212" : "#6200EE");
        this.navigationBarColor = navigationBarColor != null ? navigationBarColor : (darkMode ? "#121212" : "#FFFFFF");
        this.statusBarIconsLight = statusBarIconsLight;
        this.navigationBarIconsLight = navigationBarIconsLight;
        this.customTokens = customTokens != null ? new HashMap<>(customTokens) : new HashMap<>();
    }

    public static ThemePalette defaultLight() {
        return new ThemePalette(
                "DefaultLight",
                false,
                "#6200EE",
                "#03DAC5",
                "#FFFFFF",
                "#F5F5F5",
                "#000000",
                "#666666",
                "#FF4081",
                "#6200EE",
                "#FFFFFF",
                true,
                false,
                null
        );
    }

    public static ThemePalette defaultDark() {
        return new ThemePalette(
                "DefaultDark",
                true,
                "#BB86FC",
                "#03DAC6",
                "#121212",
                "#1E1E1E",
                "#FFFFFF",
                "#B0B0B0",
                "#FF4081",
                "#121212",
                "#121212",
                false,
                true,
                null
        );
    }

    @Override
    public String getThemeName() {
        return themeName;
    }

    @Override
    public boolean isDarkMode() {
        return darkMode;
    }

    @Override
    public String getPrimaryColor() {
        return primaryColor;
    }

    public String getSecondaryColor() {
        return secondaryColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public String getSurfaceColor() {
        return surfaceColor;
    }

    @Override
    public String getTextColor() {
        return textColor;
    }

    public String getTextSecondaryColor() {
        return textSecondaryColor;
    }

    public String getAccentColor() {
        return accentColor;
    }

    public String getStatusBarColor() {
        return statusBarColor;
    }

    public String getNavigationBarColor() {
        return navigationBarColor;
    }

    public boolean isStatusBarIconsLight() {
        return statusBarIconsLight;
    }

    public boolean isNavigationBarIconsLight() {
        return navigationBarIconsLight;
    }

    public Map<String, String> getCustomTokens() {
        return Collections.unmodifiableMap(customTokens);
    }

    public String getToken(String tokenName, String fallback) {
        String val = customTokens.get(tokenName);
        return val != null ? val : fallback;
    }
}
