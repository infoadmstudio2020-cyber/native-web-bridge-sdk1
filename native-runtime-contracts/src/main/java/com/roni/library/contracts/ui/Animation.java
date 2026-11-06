package com.roni.library.contracts.ui;

import androidx.annotation.NonNull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Component identifiers for the Animation UI category.
 */
public final class Animation {

    private Animation() {}

    public static final UIComponentType TRANSITION_ANIMATION =
            new UIComponentType(UICategory.ANIMATION, "TRANSITION_ANIMATION", "Container Transform Transition");
    public static final UIComponentType ENTER_ANIMATION =
            new UIComponentType(UICategory.ANIMATION, "ENTER_ANIMATION", "Component Slide/Fade Enter Animation");
    public static final UIComponentType EXIT_ANIMATION =
            new UIComponentType(UICategory.ANIMATION, "EXIT_ANIMATION", "Component Slide/Fade Exit Animation");
    public static final UIComponentType SHARED_ELEMENT_TRANSITION =
            new UIComponentType(UICategory.ANIMATION, "SHARED_ELEMENT_TRANSITION", "Shared Element Hero Motion");
    public static final UIComponentType LOADING_ANIMATION =
            new UIComponentType(UICategory.ANIMATION, "LOADING_ANIMATION", "Pulsing Shimmer Loading Animation");
    public static final UIComponentType COMPONENT_ANIMATION =
            new UIComponentType(UICategory.ANIMATION, "COMPONENT_ANIMATION", "Custom Property Animator");
    public static final UIComponentType MOTION_CONTROLLER =
            new UIComponentType(UICategory.ANIMATION, "MOTION_CONTROLLER", "Physics Spring Motion Controller");

    private static final List<UIComponentType> ALL = Collections.unmodifiableList(
            Arrays.asList(TRANSITION_ANIMATION, ENTER_ANIMATION, EXIT_ANIMATION, SHARED_ELEMENT_TRANSITION, LOADING_ANIMATION, COMPONENT_ANIMATION, MOTION_CONTROLLER)
    );

    @NonNull
    public static List<UIComponentType> getAllComponents() {
        return ALL;
    }
}
