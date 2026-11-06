package com.roni.library.contracts.ui;

import androidx.annotation.NonNull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Component identifiers for the Overlay UI category.
 */
public final class Overlay {

    private Overlay() {}

    public static final UIComponentType LOADING_OVERLAY =
            new UIComponentType(UICategory.OVERLAY, "LOADING_OVERLAY", "Full Screen Loading Overlay");
    public static final UIComponentType BLOCKING_OVERLAY =
            new UIComponentType(UICategory.OVERLAY, "BLOCKING_OVERLAY", "Touch Blocking Backdrop");
    public static final UIComponentType FLOATING_ACTION_BUTTON =
            new UIComponentType(UICategory.OVERLAY, "FLOATING_ACTION_BUTTON", "Floating Action Button");
    public static final UIComponentType EXTENDED_FLOATING_ACTION_BUTTON =
            new UIComponentType(UICategory.OVERLAY, "EXTENDED_FLOATING_ACTION_BUTTON", "Extended FAB with Label");
    public static final UIComponentType FLOATING_MENU =
            new UIComponentType(UICategory.OVERLAY, "FLOATING_MENU", "Speed Dial Floating Menu");
    public static final UIComponentType POPUP_WINDOW =
            new UIComponentType(UICategory.OVERLAY, "POPUP_WINDOW", "Anchored Popup Window");
    public static final UIComponentType TOOLTIP =
            new UIComponentType(UICategory.OVERLAY, "TOOLTIP", "Contextual Hover Tooltip");
    public static final UIComponentType DROPDOWN_POPUP =
            new UIComponentType(UICategory.OVERLAY, "DROPDOWN_POPUP", "Anchored Dropdown Popup");
    public static final UIComponentType CUSTOM_OVERLAY =
            new UIComponentType(UICategory.OVERLAY, "CUSTOM_OVERLAY", "Custom Coordinate Overlay");
    public static final UIComponentType SPOTLIGHT_OVERLAY =
            new UIComponentType(UICategory.OVERLAY, "SPOTLIGHT_OVERLAY", "Onboarding Spotlight Feature Coach");

    private static final List<UIComponentType> ALL = Collections.unmodifiableList(
            Arrays.asList(LOADING_OVERLAY, BLOCKING_OVERLAY, FLOATING_ACTION_BUTTON, EXTENDED_FLOATING_ACTION_BUTTON, FLOATING_MENU, POPUP_WINDOW, TOOLTIP, DROPDOWN_POPUP, CUSTOM_OVERLAY, SPOTLIGHT_OVERLAY)
    );

    @NonNull
    public static List<UIComponentType> getAllComponents() {
        return ALL;
    }
}
