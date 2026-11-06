package com.roni.library.contracts.ui;

import androidx.annotation.NonNull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Component identifiers for the Input UI category.
 */
public final class Input {

    private Input() {}

    public static final UIComponentType TEXT_FIELD =
            new UIComponentType(UICategory.INPUT, "TEXT_FIELD", "Single Line Text Input");
    public static final UIComponentType PASSWORD_FIELD =
            new UIComponentType(UICategory.INPUT, "PASSWORD_FIELD", "Masked Password Input");
    public static final UIComponentType MULTILINE_TEXT_FIELD =
            new UIComponentType(UICategory.INPUT, "MULTILINE_TEXT_FIELD", "Multiline Text Area");
    public static final UIComponentType SEARCH_INPUT =
            new UIComponentType(UICategory.INPUT, "SEARCH_INPUT", "Live Search Input");
    public static final UIComponentType CHECKBOX =
            new UIComponentType(UICategory.INPUT, "CHECKBOX", "Binary Checkbox");
    public static final UIComponentType RADIO_BUTTON =
            new UIComponentType(UICategory.INPUT, "RADIO_BUTTON", "Radio Button");
    public static final UIComponentType RADIO_GROUP =
            new UIComponentType(UICategory.INPUT, "RADIO_GROUP", "Radio Group Container");
    public static final UIComponentType SWITCH =
            new UIComponentType(UICategory.INPUT, "SWITCH", "Toggle Switch");
    public static final UIComponentType SLIDER =
            new UIComponentType(UICategory.INPUT, "SLIDER", "Continuous Value Slider");
    public static final UIComponentType RANGE_SLIDER =
            new UIComponentType(UICategory.INPUT, "RANGE_SLIDER", "Dual Thumb Range Slider");
    public static final UIComponentType DROPDOWN =
            new UIComponentType(UICategory.INPUT, "DROPDOWN", "Selection Dropdown Spinner");
    public static final UIComponentType DATE_PICKER =
            new UIComponentType(UICategory.INPUT, "DATE_PICKER", "Calendar Date Picker");
    public static final UIComponentType TIME_PICKER =
            new UIComponentType(UICategory.INPUT, "TIME_PICKER", "Clock Time Picker");
    public static final UIComponentType NUMBER_INPUT =
            new UIComponentType(UICategory.INPUT, "NUMBER_INPUT", "Numeric Stepper Input");

    private static final List<UIComponentType> ALL = Collections.unmodifiableList(
            Arrays.asList(TEXT_FIELD, PASSWORD_FIELD, MULTILINE_TEXT_FIELD, SEARCH_INPUT, CHECKBOX, RADIO_BUTTON, RADIO_GROUP, SWITCH, SLIDER, RANGE_SLIDER, DROPDOWN, DATE_PICKER, TIME_PICKER, NUMBER_INPUT)
    );

    @NonNull
    public static List<UIComponentType> getAllComponents() {
        return ALL;
    }
}
