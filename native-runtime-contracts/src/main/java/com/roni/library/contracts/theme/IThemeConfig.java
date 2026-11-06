package com.roni.library.contracts.theme;

import androidx.annotation.NonNull;

/**
 * Universal UI theme configuration contract for NativeRuntime UI and Web styling.
 *
 * @author infoadmstudio2020-cyber
 */
public interface IThemeConfig {

    /**
     * Name of the theme (e.g. "Default", "MaterialYou").
     *
     * @return theme name
     */
    @NonNull
    String getThemeName();

    /**
     * Check if dark mode is active.
     *
     * @return true if dark mode
     */
    boolean isDarkMode();

    /**
     * Hex string of primary accent color (e.g. "#6200EE").
     *
     * @return color hex
     */
    @NonNull
    String getPrimaryColor();

    /**
     * Hex string of background / surface color (e.g. "#FFFFFF" or "#121212").
     *
     * @return color hex
     */
    @NonNull
    String getSurfaceColor();

    /**
     * Hex string of primary text color.
     *
     * @return color hex
     */
    @NonNull
    String getTextColor();
}
