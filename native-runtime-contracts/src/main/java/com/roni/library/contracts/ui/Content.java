package com.roni.library.contracts.ui;

import androidx.annotation.NonNull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Component identifiers for the Content UI category.
 */
public final class Content {

    private Content() {}

    public static final UIComponentType ROOT_CONTAINER =
            new UIComponentType(UICategory.CONTENT, "ROOT_CONTAINER", "Root View Container");
    public static final UIComponentType CONTENT_CONTAINER =
            new UIComponentType(UICategory.CONTENT, "CONTENT_CONTAINER", "Main Content Container");
    public static final UIComponentType FRAME_CONTAINER =
            new UIComponentType(UICategory.CONTENT, "FRAME_CONTAINER", "Frame Container");
    public static final UIComponentType LINEAR_CONTAINER =
            new UIComponentType(UICategory.CONTENT, "LINEAR_CONTAINER", "Linear Layout Container");
    public static final UIComponentType SCROLL_CONTAINER =
            new UIComponentType(UICategory.CONTENT, "SCROLL_CONTAINER", "Scroll View Container");
    public static final UIComponentType NESTED_SCROLL_CONTAINER =
            new UIComponentType(UICategory.CONTENT, "NESTED_SCROLL_CONTAINER", "Nested Scroll Container");
    public static final UIComponentType FRAGMENT_CONTAINER =
            new UIComponentType(UICategory.CONTENT, "FRAGMENT_CONTAINER", "Fragment Host Container");
    public static final UIComponentType CUSTOM_VIEW_CONTAINER =
            new UIComponentType(UICategory.CONTENT, "CUSTOM_VIEW_CONTAINER", "Custom View Container");
    public static final UIComponentType SAFE_AREA_CONTAINER =
            new UIComponentType(UICategory.CONTENT, "SAFE_AREA_CONTAINER", "Safe Area Inset Container");
    public static final UIComponentType WEB_CONTENT_CONTAINER =
            new UIComponentType(UICategory.CONTENT, "WEB_CONTENT_CONTAINER", "Web Content Container");

    private static final List<UIComponentType> ALL = Collections.unmodifiableList(
            Arrays.asList(ROOT_CONTAINER, CONTENT_CONTAINER, FRAME_CONTAINER, LINEAR_CONTAINER, SCROLL_CONTAINER, NESTED_SCROLL_CONTAINER, FRAGMENT_CONTAINER, CUSTOM_VIEW_CONTAINER, SAFE_AREA_CONTAINER, WEB_CONTENT_CONTAINER)
    );

    @NonNull
    public static List<UIComponentType> getAllComponents() {
        return ALL;
    }
}
