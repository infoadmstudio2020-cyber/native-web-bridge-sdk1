package com.roni.library.contracts.ui;

import androidx.annotation.NonNull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Component identifiers for the AppBar UI category.
 */
public final class AppBar {

    private AppBar() {}

    public static final UIComponentType TOOLBAR =
            new UIComponentType(UICategory.APP_BAR, "TOOLBAR", "Primary Toolbar");
    public static final UIComponentType TOP_APP_BAR =
            new UIComponentType(UICategory.APP_BAR, "TOP_APP_BAR", "Top App Bar");
    public static final UIComponentType CENTER_ALIGNED_TOP_APP_BAR =
            new UIComponentType(UICategory.APP_BAR, "CENTER_ALIGNED_TOP_APP_BAR", "Center Aligned Top App Bar");
    public static final UIComponentType COLLAPSING_TOOLBAR =
            new UIComponentType(UICategory.APP_BAR, "COLLAPSING_TOOLBAR", "Collapsing Toolbar");
    public static final UIComponentType SEARCH_BAR =
            new UIComponentType(UICategory.APP_BAR, "SEARCH_BAR", "Search Bar");
    public static final UIComponentType TITLE =
            new UIComponentType(UICategory.APP_BAR, "TITLE", "AppBar Title");
    public static final UIComponentType SUBTITLE =
            new UIComponentType(UICategory.APP_BAR, "SUBTITLE", "AppBar Subtitle");
    public static final UIComponentType BACK_BUTTON =
            new UIComponentType(UICategory.APP_BAR, "BACK_BUTTON", "Navigation Back Button");
    public static final UIComponentType ACTION_MENU =
            new UIComponentType(UICategory.APP_BAR, "ACTION_MENU", "Action Menu Container");
    public static final UIComponentType ACTION_BUTTON =
            new UIComponentType(UICategory.APP_BAR, "ACTION_BUTTON", "Action Button");
    public static final UIComponentType OVERFLOW_MENU =
            new UIComponentType(UICategory.APP_BAR, "OVERFLOW_MENU", "Overflow Menu");

    private static final List<UIComponentType> ALL = Collections.unmodifiableList(
            Arrays.asList(TOOLBAR, TOP_APP_BAR, CENTER_ALIGNED_TOP_APP_BAR, COLLAPSING_TOOLBAR, SEARCH_BAR, TITLE, SUBTITLE, BACK_BUTTON, ACTION_MENU, ACTION_BUTTON, OVERFLOW_MENU)
    );

    @NonNull
    public static List<UIComponentType> getAllComponents() {
        return ALL;
    }
}
