package com.roni.library.theme;

import com.roni.library.contracts.theme.IThemeConfig;

/**
 * Observer interface for component-level theme changes.
 */
public interface ThemeObserver {

    /**
     * Called when the active theme is changed or applied.
     *
     * @param theme The newly active theme configuration
     */
    void onThemeChanged(IThemeConfig theme);
}
