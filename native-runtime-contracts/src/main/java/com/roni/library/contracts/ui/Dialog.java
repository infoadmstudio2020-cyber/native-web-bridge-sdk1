package com.roni.library.contracts.ui;

import androidx.annotation.NonNull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Component identifiers for the Dialog UI category.
 */
public final class Dialog {

    private Dialog() {}

    public static final UIComponentType ALERT_DIALOG =
            new UIComponentType(UICategory.DIALOG, "ALERT_DIALOG", "Modal Alert Dialog");
    public static final UIComponentType CONFIRMATION_DIALOG =
            new UIComponentType(UICategory.DIALOG, "CONFIRMATION_DIALOG", "Action Confirmation Dialog");
    public static final UIComponentType CUSTOM_DIALOG =
            new UIComponentType(UICategory.DIALOG, "CUSTOM_DIALOG", "Custom Layout Dialog");
    public static final UIComponentType SINGLE_CHOICE_DIALOG =
            new UIComponentType(UICategory.DIALOG, "SINGLE_CHOICE_DIALOG", "Single Choice Radio Dialog");
    public static final UIComponentType MULTI_CHOICE_DIALOG =
            new UIComponentType(UICategory.DIALOG, "MULTI_CHOICE_DIALOG", "Multi Choice Checkbox Dialog");
    public static final UIComponentType BOTTOM_SHEET_DIALOG =
            new UIComponentType(UICategory.DIALOG, "BOTTOM_SHEET_DIALOG", "Modal Bottom Sheet Dialog");
    public static final UIComponentType CUSTOM_BOTTOM_SHEET =
            new UIComponentType(UICategory.DIALOG, "CUSTOM_BOTTOM_SHEET", "Custom Content Bottom Sheet");
    public static final UIComponentType ACTION_SHEET =
            new UIComponentType(UICategory.DIALOG, "ACTION_SHEET", "iOS-Style Action Sheet");
    public static final UIComponentType FULL_SCREEN_DIALOG =
            new UIComponentType(UICategory.DIALOG, "FULL_SCREEN_DIALOG", "Full Screen Dialog");
    public static final UIComponentType PERMISSION_DIALOG =
            new UIComponentType(UICategory.DIALOG, "PERMISSION_DIALOG", "Runtime Permission Dialog");

    private static final List<UIComponentType> ALL = Collections.unmodifiableList(
            Arrays.asList(ALERT_DIALOG, CONFIRMATION_DIALOG, CUSTOM_DIALOG, SINGLE_CHOICE_DIALOG, MULTI_CHOICE_DIALOG, BOTTOM_SHEET_DIALOG, CUSTOM_BOTTOM_SHEET, ACTION_SHEET, FULL_SCREEN_DIALOG, PERMISSION_DIALOG)
    );

    @NonNull
    public static List<UIComponentType> getAllComponents() {
        return ALL;
    }
}
