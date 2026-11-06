package com.roni.library.contracts.ui;

import androidx.annotation.NonNull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Component identifiers for the SystemUI UI category.
 */
public final class SystemUI {

    private SystemUI() {}

    public static final UIComponentType STATUS_BAR =
            new UIComponentType(UICategory.SYSTEM_UI, "STATUS_BAR", "Status Bar");
    public static final UIComponentType NAVIGATION_BAR =
            new UIComponentType(UICategory.SYSTEM_UI, "NAVIGATION_BAR", "Navigation Bar");
    public static final UIComponentType EDGE_TO_EDGE =
            new UIComponentType(UICategory.SYSTEM_UI, "EDGE_TO_EDGE", "Edge-to-Edge Layout");
    public static final UIComponentType FULL_SCREEN =
            new UIComponentType(UICategory.SYSTEM_UI, "FULL_SCREEN", "Full Screen Mode");
    public static final UIComponentType IMMERSIVE_MODE =
            new UIComponentType(UICategory.SYSTEM_UI, "IMMERSIVE_MODE", "Immersive Mode");
    public static final UIComponentType WINDOW_INSETS =
            new UIComponentType(UICategory.SYSTEM_UI, "WINDOW_INSETS", "Window Insets Handler");
    public static final UIComponentType DISPLAY_CUTOUT =
            new UIComponentType(UICategory.SYSTEM_UI, "DISPLAY_CUTOUT", "Display Cutout Handler");
    public static final UIComponentType SCREEN_ORIENTATION =
            new UIComponentType(UICategory.SYSTEM_UI, "SCREEN_ORIENTATION", "Screen Orientation Controller");
    public static final UIComponentType KEEP_SCREEN_ON =
            new UIComponentType(UICategory.SYSTEM_UI, "KEEP_SCREEN_ON", "Keep Screen On Flag");

    private static final List<UIComponentType> ALL = Collections.unmodifiableList(
            Arrays.asList(STATUS_BAR, NAVIGATION_BAR, EDGE_TO_EDGE, FULL_SCREEN, IMMERSIVE_MODE, WINDOW_INSETS, DISPLAY_CUTOUT, SCREEN_ORIENTATION, KEEP_SCREEN_ON)
    );

    @NonNull
    public static List<UIComponentType> getAllComponents() {
        return ALL;
    }
}
