package com.roni.library.contracts.ui;

import androidx.annotation.NonNull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Component identifiers for the Navigation UI category.
 */
public final class Navigation {

    private Navigation() {}

    public static final UIComponentType BOTTOM_NAVIGATION =
            new UIComponentType(UICategory.NAVIGATION, "BOTTOM_NAVIGATION", "Bottom Navigation Bar");
    public static final UIComponentType NAVIGATION_DRAWER =
            new UIComponentType(UICategory.NAVIGATION, "NAVIGATION_DRAWER", "Navigation Drawer");
    public static final UIComponentType NAVIGATION_RAIL =
            new UIComponentType(UICategory.NAVIGATION, "NAVIGATION_RAIL", "Navigation Rail");
    public static final UIComponentType TAB_NAVIGATION =
            new UIComponentType(UICategory.NAVIGATION, "TAB_NAVIGATION", "Tab Navigation Controller");
    public static final UIComponentType TAB_LAYOUT =
            new UIComponentType(UICategory.NAVIGATION, "TAB_LAYOUT", "Tab Layout Header");
    public static final UIComponentType NAVIGATION_CONTROLLER =
            new UIComponentType(UICategory.NAVIGATION, "NAVIGATION_CONTROLLER", "Navigation Controller");
    public static final UIComponentType NAVIGATION_STACK =
            new UIComponentType(UICategory.NAVIGATION, "NAVIGATION_STACK", "Navigation Backstack");
    public static final UIComponentType BACK_NAVIGATION =
            new UIComponentType(UICategory.NAVIGATION, "BACK_NAVIGATION", "Back Navigation Dispatcher");
    public static final UIComponentType NAVIGATION_BADGE =
            new UIComponentType(UICategory.NAVIGATION, "NAVIGATION_BADGE", "Navigation Badge");
    public static final UIComponentType NAVIGATION_INDICATOR =
            new UIComponentType(UICategory.NAVIGATION, "NAVIGATION_INDICATOR", "Navigation Active Indicator");

    private static final List<UIComponentType> ALL = Collections.unmodifiableList(
            Arrays.asList(BOTTOM_NAVIGATION, NAVIGATION_DRAWER, NAVIGATION_RAIL, TAB_NAVIGATION, TAB_LAYOUT, NAVIGATION_CONTROLLER, NAVIGATION_STACK, BACK_NAVIGATION, NAVIGATION_BADGE, NAVIGATION_INDICATOR)
    );

    @NonNull
    public static List<UIComponentType> getAllComponents() {
        return ALL;
    }
}
