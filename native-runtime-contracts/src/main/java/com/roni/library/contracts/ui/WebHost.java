package com.roni.library.contracts.ui;

import androidx.annotation.NonNull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Component identifiers for the WebHost UI category.
 */
public final class WebHost {

    private WebHost() {}

    public static final UIComponentType WEB_VIEW_CONTAINER =
            new UIComponentType(UICategory.WEB_HOST, "WEB_VIEW_CONTAINER", "Hardware Accelerated WebView Container");
    public static final UIComponentType WEB_LOADING_VIEW =
            new UIComponentType(UICategory.WEB_HOST, "WEB_LOADING_VIEW", "Web Page Progress Shimmer");
    public static final UIComponentType WEB_ERROR_VIEW =
            new UIComponentType(UICategory.WEB_HOST, "WEB_ERROR_VIEW", "Web Connection Error Visualizer");
    public static final UIComponentType WEB_PERMISSION_VIEW =
            new UIComponentType(UICategory.WEB_HOST, "WEB_PERMISSION_VIEW", "Web Geolocation & Media Permission Prompt");
    public static final UIComponentType WEB_NAVIGATION_CONTROLS =
            new UIComponentType(UICategory.WEB_HOST, "WEB_NAVIGATION_CONTROLS", "Web Back/Forward/Reload Toolbar");
    public static final UIComponentType WEB_PROGRESS_INDICATOR =
            new UIComponentType(UICategory.WEB_HOST, "WEB_PROGRESS_INDICATOR", "Top Web Progress Bar");
    public static final UIComponentType WEB_OFFLINE_VIEW =
            new UIComponentType(UICategory.WEB_HOST, "WEB_OFFLINE_VIEW", "Network Disconnected Offline Screen");

    private static final List<UIComponentType> ALL = Collections.unmodifiableList(
            Arrays.asList(WEB_VIEW_CONTAINER, WEB_LOADING_VIEW, WEB_ERROR_VIEW, WEB_PERMISSION_VIEW, WEB_NAVIGATION_CONTROLS, WEB_PROGRESS_INDICATOR, WEB_OFFLINE_VIEW)
    );

    @NonNull
    public static List<UIComponentType> getAllComponents() {
        return ALL;
    }
}
