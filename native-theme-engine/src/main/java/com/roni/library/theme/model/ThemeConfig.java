package com.roni.library.theme.model;

import com.roni.library.contracts.theme.IThemeConfig;
import com.roni.library.theme.ColorTokenResolver;
import java.io.Serializable;

/**
 * Comprehensive theme configuration model satisfying IThemeConfig contract
 * and NativeThemeEngine specifications.
 */
public class ThemeConfig implements IThemeConfig, Serializable {

    public enum ThemeMode {
        SYSTEM,
        LIGHT,
        DARK
    }

    private final String themeName;
    private final ThemeMode themeMode;
    private final boolean darkMode;
    private final int primaryColor;
    private final int secondaryColor;
    private final int backgroundColor;
    private final int surfaceColor;
    private final int textColor;
    private final int statusBarColor;
    private final int navigationBarColor;
    private final boolean statusBarIconsLight;
    private final boolean navigationBarIconsLight;

    public ThemeConfig(String themeName,
                       ThemeMode themeMode,
                       boolean darkMode,
                       int primaryColor,
                       int secondaryColor,
                       int backgroundColor,
                       int surfaceColor,
                       int textColor,
                       int statusBarColor,
                       int navigationBarColor,
                       boolean statusBarIconsLight,
                       boolean navigationBarIconsLight) {
        this.themeName = themeName != null ? themeName : (darkMode ? "Dark" : "Light");
        this.themeMode = themeMode != null ? themeMode : (darkMode ? ThemeMode.DARK : ThemeMode.LIGHT);
        this.darkMode = darkMode;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.backgroundColor = backgroundColor;
        this.surfaceColor = surfaceColor;
        this.textColor = textColor;
        this.statusBarColor = statusBarColor;
        this.navigationBarColor = navigationBarColor;
        this.statusBarIconsLight = statusBarIconsLight;
        this.navigationBarIconsLight = navigationBarIconsLight;
    }

    public static ThemeConfig defaultLight() {
        return new ThemeConfig(
                "DefaultLight",
                ThemeMode.LIGHT,
                false,
                0xFF6200EE,
                0xFF03DAC5,
                0xFFFFFFFF,
                0xFFF5F5F5,
                0xFF000000,
                0xFF6200EE,
                0xFFFFFFFF,
                true,
                false
        );
    }

    public static ThemeConfig defaultDark() {
        return new ThemeConfig(
                "DefaultDark",
                ThemeMode.DARK,
                true,
                0xFFBB86FC,
                0xFF03DAC6,
                0xFF121212,
                0xFF1E1E1E,
                0xFFFFFFFF,
                0xFF121212,
                0xFF121212,
                false,
                true
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
        return ColorTokenResolver.toHexString(primaryColor, false);
    }

    @Override
    public String getSurfaceColor() {
        return ColorTokenResolver.toHexString(surfaceColor, false);
    }

    @Override
    public String getTextColor() {
        return ColorTokenResolver.toHexString(textColor, false);
    }

    public ThemeMode getThemeMode() {
        return themeMode;
    }

    public int getPrimaryColorInt() {
        return primaryColor;
    }

    public int getSecondaryColor() {
        return secondaryColor;
    }

    public String getSecondaryColorHex() {
        return ColorTokenResolver.toHexString(secondaryColor, false);
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public String getBackgroundColorHex() {
        return ColorTokenResolver.toHexString(backgroundColor, false);
    }

    public int getSurfaceColorInt() {
        return surfaceColor;
    }

    public int getTextColorInt() {
        return textColor;
    }

    public int getStatusBarColor() {
        return statusBarColor;
    }

    public String getStatusBarColorHex() {
        return ColorTokenResolver.toHexString(statusBarColor, false);
    }

    public int getNavigationBarColor() {
        return navigationBarColor;
    }

    public String getNavigationBarColorHex() {
        return ColorTokenResolver.toHexString(navigationBarColor, false);
    }

    public boolean isStatusBarIconsLight() {
        return statusBarIconsLight;
    }

    public boolean isNavigationBarIconsLight() {
        return navigationBarIconsLight;
    }
}
