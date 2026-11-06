package com.roni.library.themeengine.model;

/**
 * Forwarding alias to com.roni.library.theme.model.ThemeConfig for backward compatibility.
 */
public class ThemeConfig extends com.roni.library.theme.model.ThemeConfig {

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
        super(themeName, themeMode, darkMode, primaryColor, secondaryColor, backgroundColor,
                surfaceColor, textColor, statusBarColor, navigationBarColor, statusBarIconsLight, navigationBarIconsLight);
    }
}
