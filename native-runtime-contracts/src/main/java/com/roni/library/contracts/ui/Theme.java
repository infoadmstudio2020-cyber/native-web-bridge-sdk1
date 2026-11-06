package com.roni.library.contracts.ui;

import androidx.annotation.NonNull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Component identifiers for the Theme UI category.
 */
public final class Theme {

    private Theme() {}

    public static final UIComponentType THEME_CONTROLLER =
            new UIComponentType(UICategory.THEME, "THEME_CONTROLLER", "Runtime Theme Mode Controller");
    public static final UIComponentType THEME_MODE =
            new UIComponentType(UICategory.THEME, "THEME_MODE", "Dark/Light/System Theme Mode");
    public static final UIComponentType COLOR_SCHEME =
            new UIComponentType(UICategory.THEME, "COLOR_SCHEME", "Dynamic Color Palette Scheme");
    public static final UIComponentType TYPOGRAPHY =
            new UIComponentType(UICategory.THEME, "TYPOGRAPHY", "Typography Scale & Font Family");
    public static final UIComponentType SHAPE_STYLE =
            new UIComponentType(UICategory.THEME, "SHAPE_STYLE", "Corner Radius & Elevation Shape Style");
    public static final UIComponentType COMPONENT_STYLE =
            new UIComponentType(UICategory.THEME, "COMPONENT_STYLE", "Component Token Style Overrides");
    public static final UIComponentType SYSTEM_BAR_THEME =
            new UIComponentType(UICategory.THEME, "SYSTEM_BAR_THEME", "Light/Dark System Bars Styler");
    public static final UIComponentType DYNAMIC_THEME =
            new UIComponentType(UICategory.THEME, "DYNAMIC_THEME", "Material You Dynamic Wallpaper Theme");
    public static final UIComponentType THEME_TRANSITION =
            new UIComponentType(UICategory.THEME, "THEME_TRANSITION", "Animated Theme Transition Crossfade");

    private static final List<UIComponentType> ALL = Collections.unmodifiableList(
            Arrays.asList(THEME_CONTROLLER, THEME_MODE, COLOR_SCHEME, TYPOGRAPHY, SHAPE_STYLE, COMPONENT_STYLE, SYSTEM_BAR_THEME, DYNAMIC_THEME, THEME_TRANSITION)
    );

    @NonNull
    public static List<UIComponentType> getAllComponents() {
        return ALL;
    }
}
