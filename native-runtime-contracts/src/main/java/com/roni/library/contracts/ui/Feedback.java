package com.roni.library.contracts.ui;

import androidx.annotation.NonNull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Component identifiers for the Feedback UI category.
 */
public final class Feedback {

    private Feedback() {}

    public static final UIComponentType TOAST =
            new UIComponentType(UICategory.FEEDBACK, "TOAST", "Transient Toast Notification");
    public static final UIComponentType SNACKBAR =
            new UIComponentType(UICategory.FEEDBACK, "SNACKBAR", "Interactive Snackbar");
    public static final UIComponentType LOADING_INDICATOR =
            new UIComponentType(UICategory.FEEDBACK, "LOADING_INDICATOR", "Global Loading Spinner");
    public static final UIComponentType PROGRESS_INDICATOR =
            new UIComponentType(UICategory.FEEDBACK, "PROGRESS_INDICATOR", "Determinate Progress Indicator");
    public static final UIComponentType LINEAR_PROGRESS =
            new UIComponentType(UICategory.FEEDBACK, "LINEAR_PROGRESS", "Linear Progress Bar");
    public static final UIComponentType CIRCULAR_PROGRESS =
            new UIComponentType(UICategory.FEEDBACK, "CIRCULAR_PROGRESS", "Circular Progress Indicator");
    public static final UIComponentType SUCCESS_MESSAGE =
            new UIComponentType(UICategory.FEEDBACK, "SUCCESS_MESSAGE", "Success Notification Banner");
    public static final UIComponentType ERROR_MESSAGE =
            new UIComponentType(UICategory.FEEDBACK, "ERROR_MESSAGE", "Error Notification Banner");
    public static final UIComponentType WARNING_MESSAGE =
            new UIComponentType(UICategory.FEEDBACK, "WARNING_MESSAGE", "Warning Notification Banner");
    public static final UIComponentType EMPTY_STATE =
            new UIComponentType(UICategory.FEEDBACK, "EMPTY_STATE", "Empty State Visualizer");
    public static final UIComponentType ERROR_STATE =
            new UIComponentType(UICategory.FEEDBACK, "ERROR_STATE", "Error State Visualizer");

    private static final List<UIComponentType> ALL = Collections.unmodifiableList(
            Arrays.asList(TOAST, SNACKBAR, LOADING_INDICATOR, PROGRESS_INDICATOR, LINEAR_PROGRESS, CIRCULAR_PROGRESS, SUCCESS_MESSAGE, ERROR_MESSAGE, WARNING_MESSAGE, EMPTY_STATE, ERROR_STATE)
    );

    @NonNull
    public static List<UIComponentType> getAllComponents() {
        return ALL;
    }
}
