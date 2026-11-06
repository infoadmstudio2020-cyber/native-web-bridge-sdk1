package com.roni.library.ui.registry;

import android.content.Context;
import androidx.annotation.NonNull;
import com.roni.library.contracts.ui.*;
import com.roni.library.ui.components.systemui.*;
import com.roni.library.ui.components.appbar.*;
import com.roni.library.ui.components.navigation.*;
import com.roni.library.ui.components.content.*;
import com.roni.library.ui.components.dialog.*;
import com.roni.library.ui.components.feedback.*;
import com.roni.library.ui.components.overlay.*;
import com.roni.library.ui.components.input.*;
import com.roni.library.ui.components.webhost.*;
import com.roni.library.ui.components.theme.*;
import com.roni.library.ui.components.lifecycle.*;
import com.roni.library.ui.components.animation.*;

public final class StandardUIComponentRegistryInitializer {

    private StandardUIComponentRegistryInitializer() {}

    public static void initializeDefaults(@NonNull UIComponentRegistry registry) {
        // SystemUI
        register(registry, SystemUI.STATUS_BAR, StatusBarComponent::new, 100);
        register(registry, SystemUI.NAVIGATION_BAR, NavigationBarComponent::new, 99);
        register(registry, SystemUI.EDGE_TO_EDGE, EdgeToEdgeComponent::new, 98);
        register(registry, SystemUI.FULL_SCREEN, FullScreenComponent::new, 97);
        register(registry, SystemUI.IMMERSIVE_MODE, ImmersiveModeComponent::new, 96);
        register(registry, SystemUI.WINDOW_INSETS, WindowInsetsComponent::new, 95);
        register(registry, SystemUI.DISPLAY_CUTOUT, DisplayCutoutComponent::new, 94);
        register(registry, SystemUI.SCREEN_ORIENTATION, ScreenOrientationComponent::new, 93);
        register(registry, SystemUI.KEEP_SCREEN_ON, KeepScreenOnComponent::new, 92);

        // AppBar
        register(registry, AppBar.TOOLBAR, ToolbarComponent::new, 90);
        register(registry, AppBar.TOP_APP_BAR, TopAppBarComponent::new, 89);
        register(registry, AppBar.CENTER_ALIGNED_TOP_APP_BAR, CenterAlignedTopAppBarComponent::new, 88);
        register(registry, AppBar.COLLAPSING_TOOLBAR, CollapsingToolbarComponent::new, 87);
        register(registry, AppBar.SEARCH_BAR, SearchBarComponent::new, 86);
        register(registry, AppBar.TITLE, TitleComponent::new, 85);
        register(registry, AppBar.SUBTITLE, SubtitleComponent::new, 84);
        register(registry, AppBar.BACK_BUTTON, BackButtonComponent::new, 83);
        register(registry, AppBar.ACTION_MENU, ActionMenuComponent::new, 82);
        register(registry, AppBar.ACTION_BUTTON, ActionButtonComponent::new, 81);
        register(registry, AppBar.OVERFLOW_MENU, OverflowMenuComponent::new, 80);

        // Navigation
        register(registry, Navigation.BOTTOM_NAVIGATION, BottomNavigationComponent::new, 75);
        register(registry, Navigation.NAVIGATION_DRAWER, NavigationDrawerComponent::new, 74);
        register(registry, Navigation.NAVIGATION_RAIL, NavigationRailComponent::new, 73);
        register(registry, Navigation.TAB_NAVIGATION, TabNavigationComponent::new, 72);
        register(registry, Navigation.TAB_LAYOUT, TabLayoutComponent::new, 71);
        register(registry, Navigation.NAVIGATION_CONTROLLER, NavigationControllerComponent::new, 70);
        register(registry, Navigation.NAVIGATION_STACK, NavigationStackComponent::new, 69);
        register(registry, Navigation.BACK_NAVIGATION, BackNavigationComponent::new, 68);
        register(registry, Navigation.NAVIGATION_BADGE, NavigationBadgeComponent::new, 67);
        register(registry, Navigation.NAVIGATION_INDICATOR, NavigationIndicatorComponent::new, 66);

        // Content
        register(registry, Content.ROOT_CONTAINER, RootContainerComponent::new, 60);
        register(registry, Content.CONTENT_CONTAINER, ContentContainerComponent::new, 59);
        register(registry, Content.FRAME_CONTAINER, FrameContainerComponent::new, 58);
        register(registry, Content.LINEAR_CONTAINER, LinearContainerComponent::new, 57);
        register(registry, Content.SCROLL_CONTAINER, ScrollContainerComponent::new, 56);
        register(registry, Content.NESTED_SCROLL_CONTAINER, NestedScrollContainerComponent::new, 55);
        register(registry, Content.FRAGMENT_CONTAINER, FragmentContainerComponent::new, 54);
        register(registry, Content.CUSTOM_VIEW_CONTAINER, CustomViewContainerComponent::new, 53);
        register(registry, Content.SAFE_AREA_CONTAINER, SafeAreaContainerComponent::new, 52);
        register(registry, Content.WEB_CONTENT_CONTAINER, WebContentContainerComponent::new, 51);

        // Dialog
        register(registry, Dialog.ALERT_DIALOG, AlertDialogComponent::new, 45);
        register(registry, Dialog.CONFIRMATION_DIALOG, ConfirmationDialogComponent::new, 44);
        register(registry, Dialog.CUSTOM_DIALOG, CustomDialogComponent::new, 43);
        register(registry, Dialog.SINGLE_CHOICE_DIALOG, SingleChoiceDialogComponent::new, 42);
        register(registry, Dialog.MULTI_CHOICE_DIALOG, MultiChoiceDialogComponent::new, 41);
        register(registry, Dialog.BOTTOM_SHEET_DIALOG, BottomSheetDialogComponent::new, 40);
        register(registry, Dialog.CUSTOM_BOTTOM_SHEET, CustomBottomSheetComponent::new, 39);
        register(registry, Dialog.ACTION_SHEET, ActionSheetComponent::new, 38);
        register(registry, Dialog.FULL_SCREEN_DIALOG, FullScreenDialogComponent::new, 37);
        register(registry, Dialog.PERMISSION_DIALOG, PermissionDialogComponent::new, 36);

        // Feedback
        register(registry, Feedback.TOAST, ToastComponent::new, 30);
        register(registry, Feedback.SNACKBAR, SnackbarComponent::new, 29);
        register(registry, Feedback.LOADING_INDICATOR, LoadingIndicatorComponent::new, 28);
        register(registry, Feedback.PROGRESS_INDICATOR, ProgressIndicatorComponent::new, 27);
        register(registry, Feedback.LINEAR_PROGRESS, LinearProgressComponent::new, 26);
        register(registry, Feedback.CIRCULAR_PROGRESS, CircularProgressComponent::new, 25);
        register(registry, Feedback.SUCCESS_MESSAGE, SuccessMessageComponent::new, 24);
        register(registry, Feedback.ERROR_MESSAGE, ErrorMessageComponent::new, 23);
        register(registry, Feedback.WARNING_MESSAGE, WarningMessageComponent::new, 22);
        register(registry, Feedback.EMPTY_STATE, EmptyStateComponent::new, 21);
        register(registry, Feedback.ERROR_STATE, ErrorStateComponent::new, 20);

        // Overlay
        register(registry, Overlay.LOADING_OVERLAY, LoadingOverlayComponent::new, 15);
        register(registry, Overlay.BLOCKING_OVERLAY, BlockingOverlayComponent::new, 14);
        register(registry, Overlay.FLOATING_ACTION_BUTTON, FloatingActionButtonComponent::new, 13);
        register(registry, Overlay.EXTENDED_FLOATING_ACTION_BUTTON, ExtendedFloatingActionButtonComponent::new, 12);
        register(registry, Overlay.FLOATING_MENU, FloatingMenuComponent::new, 11);
        register(registry, Overlay.POPUP_WINDOW, PopupWindowComponent::new, 10);
        register(registry, Overlay.TOOLTIP, TooltipComponent::new, 9);
        register(registry, Overlay.DROPDOWN_POPUP, DropdownPopupComponent::new, 8);
        register(registry, Overlay.CUSTOM_OVERLAY, CustomOverlayComponent::new, 7);
        register(registry, Overlay.SPOTLIGHT_OVERLAY, SpotlightOverlayComponent::new, 6);

        // Input
        register(registry, Input.TEXT_FIELD, TextFieldComponent::new, 5);
        register(registry, Input.PASSWORD_FIELD, PasswordFieldComponent::new, 5);
        register(registry, Input.MULTILINE_TEXT_FIELD, MultilineTextFieldComponent::new, 5);
        register(registry, Input.SEARCH_INPUT, SearchInputComponent::new, 5);
        register(registry, Input.CHECKBOX, CheckboxComponent::new, 5);
        register(registry, Input.RADIO_BUTTON, RadioButtonComponent::new, 5);
        register(registry, Input.RADIO_GROUP, RadioGroupComponent::new, 5);
        register(registry, Input.SWITCH, SwitchComponent::new, 5);
        register(registry, Input.SLIDER, SliderComponent::new, 5);
        register(registry, Input.RANGE_SLIDER, RangeSliderComponent::new, 5);
        register(registry, Input.DROPDOWN, DropdownComponent::new, 5);
        register(registry, Input.DATE_PICKER, DatePickerComponent::new, 5);
        register(registry, Input.TIME_PICKER, TimePickerComponent::new, 5);
        register(registry, Input.NUMBER_INPUT, NumberInputComponent::new, 5);

        // WebHost
        register(registry, WebHost.WEB_VIEW_CONTAINER, WebViewContainerComponent::new, 4);
        register(registry, WebHost.WEB_LOADING_VIEW, WebLoadingViewComponent::new, 4);
        register(registry, WebHost.WEB_ERROR_VIEW, WebErrorViewComponent::new, 4);
        register(registry, WebHost.WEB_PERMISSION_VIEW, WebPermissionViewComponent::new, 4);
        register(registry, WebHost.WEB_NAVIGATION_CONTROLS, WebNavigationControlsComponent::new, 4);
        register(registry, WebHost.WEB_PROGRESS_INDICATOR, WebProgressIndicatorComponent::new, 4);
        register(registry, WebHost.WEB_OFFLINE_VIEW, WebOfflineViewComponent::new, 4);

        // Theme
        register(registry, Theme.THEME_CONTROLLER, ThemeControllerComponent::new, 3);
        register(registry, Theme.THEME_MODE, ThemeModeComponent::new, 3);
        register(registry, Theme.COLOR_SCHEME, ColorSchemeComponent::new, 3);
        register(registry, Theme.TYPOGRAPHY, TypographyComponent::new, 3);
        register(registry, Theme.SHAPE_STYLE, ShapeStyleComponent::new, 3);
        register(registry, Theme.COMPONENT_STYLE, ComponentStyleComponent::new, 3);
        register(registry, Theme.SYSTEM_BAR_THEME, SystemBarThemeComponent::new, 3);
        register(registry, Theme.DYNAMIC_THEME, DynamicThemeComponent::new, 3);
        register(registry, Theme.THEME_TRANSITION, ThemeTransitionComponent::new, 3);

        // Lifecycle
        register(registry, Lifecycle.UI_STATE_CONTROLLER, UIStateControllerComponent::new, 2);
        register(registry, Lifecycle.COMPONENT_LIFECYCLE, ComponentLifecycleComponent::new, 2);
        register(registry, Lifecycle.CONFIGURATION_CHANGE_HANDLER, ConfigurationChangeHandlerComponent::new, 2);
        register(registry, Lifecycle.VISIBILITY_CONTROLLER, VisibilityControllerComponent::new, 2);
        register(registry, Lifecycle.STATE_RESTORATION, StateRestorationComponent::new, 2);
        register(registry, Lifecycle.SCREEN_STATE_CONTROLLER, ScreenStateControllerComponent::new, 2);

        // Animation
        register(registry, Animation.TRANSITION_ANIMATION, TransitionAnimationComponent::new, 1);
        register(registry, Animation.ENTER_ANIMATION, EnterAnimationComponent::new, 1);
        register(registry, Animation.EXIT_ANIMATION, ExitAnimationComponent::new, 1);
        register(registry, Animation.SHARED_ELEMENT_TRANSITION, SharedElementTransitionComponent::new, 1);
        register(registry, Animation.LOADING_ANIMATION, LoadingAnimationComponent::new, 1);
        register(registry, Animation.COMPONENT_ANIMATION, ComponentAnimationComponent::new, 1);
        register(registry, Animation.MOTION_CONTROLLER, MotionControllerComponent::new, 1);
    }

    private interface ComponentFactory {
        IUIComponent create(IUIComponentDescriptor descriptor);
    }

    private static void register(UIComponentRegistry registry,
                                 UIComponentType type,
                                 ComponentFactory factory,
                                 int priority) {
        UIComponentDescriptor desc = new UIComponentDescriptor(
                type,
                type.getDisplayName(),
                "Standard NativeRuntime UI Component: " + type.getDisplayName(),
                false, // default disabled unless enabled via config
                priority
        );
        registry.registerDescriptor(desc);
        registry.registerProvider(new UIComponentProvider(type) {
            @NonNull
            @Override
            public IUIComponent createComponent(@NonNull Context context, @NonNull IUIComponentDescriptor descriptor) {
                return factory.create(descriptor);
            }
        });
    }
}
