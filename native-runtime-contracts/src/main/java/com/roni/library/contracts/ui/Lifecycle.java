package com.roni.library.contracts.ui;

import androidx.annotation.NonNull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Component identifiers for the Lifecycle UI category.
 */
public final class Lifecycle {

    private Lifecycle() {}

    public static final UIComponentType UI_STATE_CONTROLLER =
            new UIComponentType(UICategory.LIFECYCLE, "UI_STATE_CONTROLLER", "Master UI State Controller");
    public static final UIComponentType COMPONENT_LIFECYCLE =
            new UIComponentType(UICategory.LIFECYCLE, "COMPONENT_LIFECYCLE", "Component Lifecycle Dispatcher");
    public static final UIComponentType CONFIGURATION_CHANGE_HANDLER =
            new UIComponentType(UICategory.LIFECYCLE, "CONFIGURATION_CHANGE_HANDLER", "Orientation & Locale Change Handler");
    public static final UIComponentType VISIBILITY_CONTROLLER =
            new UIComponentType(UICategory.LIFECYCLE, "VISIBILITY_CONTROLLER", "Coordinated Visibility Controller");
    public static final UIComponentType STATE_RESTORATION =
            new UIComponentType(UICategory.LIFECYCLE, "STATE_RESTORATION", "Bundle Save & Restore Coordinator");
    public static final UIComponentType SCREEN_STATE_CONTROLLER =
            new UIComponentType(UICategory.LIFECYCLE, "SCREEN_STATE_CONTROLLER", "Screen On/Off & Focus Controller");

    private static final List<UIComponentType> ALL = Collections.unmodifiableList(
            Arrays.asList(UI_STATE_CONTROLLER, COMPONENT_LIFECYCLE, CONFIGURATION_CHANGE_HANDLER, VISIBILITY_CONTROLLER, STATE_RESTORATION, SCREEN_STATE_CONTROLLER)
    );

    @NonNull
    public static List<UIComponentType> getAllComponents() {
        return ALL;
    }
}
