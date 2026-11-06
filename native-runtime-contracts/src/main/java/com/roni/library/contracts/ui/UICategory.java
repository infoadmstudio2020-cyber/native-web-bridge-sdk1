package com.roni.library.contracts.ui;

import androidx.annotation.NonNull;
import java.io.Serializable;

/**
 * The 12 Main UI Categories of the NativeRuntime UI Framework.
 */
public enum UICategory implements Serializable {
    SYSTEM_UI("SystemUI", "System bar, insets, display cutout, and orientation controls"),
    APP_BAR("AppBar", "Toolbars, top app bars, search bars, and action menus"),
    NAVIGATION("Navigation", "Bottom navigation, drawers, rails, tabs, and back navigation"),
    CONTENT("Content", "Root containers, content frames, scrolling, and view containers"),
    DIALOG("Dialog", "Alert dialogs, confirmation, bottom sheets, and action sheets"),
    FEEDBACK("Feedback", "Toasts, snackbars, loading indicators, and message states"),
    OVERLAY("Overlay", "Loading overlays, FABs, floating menus, popups, and tooltips"),
    INPUT("Input", "Form fields, switches, sliders, pickers, and selection controls"),
    WEB_HOST("WebHost", "WebView container, web loading, errors, and web navigation"),
    THEME("Theme", "Theme controllers, color schemes, typography, and styles"),
    LIFECYCLE("Lifecycle", "UI state controller, component lifecycle, and state restoration"),
    ANIMATION("Animation", "Transitions, enter/exit animations, and motion controllers");

    private final String displayName;
    private final String description;

    UICategory(@NonNull String displayName, @NonNull String description) {
        this.displayName = displayName;
        this.description = description;
    }

    @NonNull
    public String getDisplayName() {
        return displayName;
    }

    @NonNull
    public String getDescription() {
        return description;
    }
}
