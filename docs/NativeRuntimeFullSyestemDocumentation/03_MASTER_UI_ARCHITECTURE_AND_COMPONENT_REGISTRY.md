# NativeRuntimeUI Master Architecture & Component Registry Reference

## 1. Executive Summary

NativeRuntimeUI has been elevated into a complete, modular, registry-driven Native Android UI Framework. It unifies UI orchestration across all 15 libraries of the NativeRuntime Android Ecosystem into a decoupled, declarative architecture.

### Key Architectural Pillars
- **Zero View Boilerplate for Consumers**: Developers configure UI by extending `NativeRuntimeActivity` without writing manual Toolbar, StatusBar, Navigation, or Dialog inflation code.
- **Strict 3-Tier Priority System**:
  `Individual Component Override > Category Default > Global Default`
- **12 Main UI Categories**: SystemUI, AppBar, Navigation, Content, Dialog, Feedback, Overlay, Input, WebHost, Theme, Lifecycle, and Animation.
- **Microkernel Component Registry**: Every component possesses an explicit descriptor, provider, controller, state envelope, and lifecycle state machine.

---

## 2. UI Management Architecture

```
+-----------------------------------------------------------------------------------+
|                             NativeRuntimeActivity                                 |
|                               (Consumer Facade)                                   |
+-----------------------------------------------------------------------------------+
                                         |
                                         v
+-----------------------------------------------------------------------------------+
|                                 NativeUIHost                                      |
|  +-----------------------+ +-------------------------+ +-----------------------+  |
|  |  UIComponentRegistry  | | UIConfigurationManager  | | UICompositionManager  |  |
|  +-----------------------+ +-------------------------+ +-----------------------+  |
|  +-----------------------+ +-------------------------+ +-----------------------+  |
|  |  UIVisibilityManager  | |     UIStateManager      | |   UIEventDispatcher   |  |
|  +-----------------------+ +-------------------------+ +-----------------------+  |
|  +-----------------------+ +-------------------------+ +-----------------------+  |
|  |      UIRenderer       | |    ComponentRenderer    | |    NativeUIRootView   |  |
|  +-----------------------+ +-------------------------+ +-----------------------+  |
+-----------------------------------------------------------------------------------+
```

### Core Architecture Components:
1. **UIComponentRegistry**: Central discovery engine indexing all 12 categories, component descriptors, and dynamic component providers.
2. **UIComponentDescriptor**: Immutable metadata defining component category, priority, default state, and dependencies.
3. **UIComponentProvider**: Factory creating component instances on-demand.
4. **UIComponentController**: Handles attachment, initialization, rendering callbacks, visibility changes, and destruction.
5. **UIConfigurationManager**: Implements the 3-tier priority matrix, component properties, and global/category switches.
6. **UICompositionManager**: Orchestrates view hierarchy insertion into dedicated slot containers (AppBar, Content, Navigation, Overlay).
7. **UIVisibilityManager**: Thread-safe visibility state management and listener dispatch.
8. **UIRenderer & ComponentRenderer**: High-performance view rendering pipeline constructing `NativeUIRootView`.
9. **UIStateManager**: Reactive key-value state management with automated Bundle save/restore support.
10. **UIEventDispatcher**: Decoupled pub/sub event bus supporting wildcard and component-specific event subscriptions.
11. **NativeUIHost**: Aggregates all management systems and wraps existing managers (`DialogManager`, `ToolbarManager`, `NavigationManager`, `LoadingOverlayManager`).
12. **NativeUIRootView**: Composed Android `FrameLayout` containing:
    - Status bar spacer
    - AppBar container
    - Central weighted Content container
    - Navigation container
    - Navigation bar spacer
    - Top-level modal/overlay container

---

## 3. The 12 UI Categories & Sub-component Specifications

### 1. SystemUI (Category ID: `SYSTEM_UI`)
Handles OS-level system window decoration, insets, cutouts, and screen parameters:
- `SystemUI.STATUS_BAR`: Status bar color and icon appearance.
- `SystemUI.NAVIGATION_BAR`: Navigation bar styling and contrast.
- `SystemUI.EDGE_TO_EDGE`: Extends view hierarchy behind system window decorations.
- `SystemUI.FULL_SCREEN`: Fullscreen window flags.
- `SystemUI.IMMERSIVE_MODE`: Sticky immersive navigation hide mode.
- `SystemUI.WINDOW_INSETS`: WindowInsets listener and padding calculator.
- `SystemUI.DISPLAY_CUTOUT`: Notched display and cutout handling.
- `SystemUI.SCREEN_ORIENTATION`: Orientation lock/rotation controller.
- `SystemUI.KEEP_SCREEN_ON`: WakeLock-free window flag for continuous display.

### 2. AppBar (Category ID: `APP_BAR`)
Primary top navigation and contextual action controls:
- `AppBar.TOOLBAR`: Primary action bar integrating `ToolbarManager`.
- `AppBar.TOP_APP_BAR`: Standard Android TopAppBar container.
- `AppBar.CENTER_ALIGNED_TOP_APP_BAR`: Title centered navigation header.
- `AppBar.COLLAPSING_TOOLBAR`: Scroll-reactive collapsing header.
- `AppBar.SEARCH_BAR`: Embedded filter and search input.
- `AppBar.TITLE`, `AppBar.SUBTITLE`: Textual header labels.
- `AppBar.BACK_BUTTON`: Up/back navigation arrow.
- `AppBar.ACTION_MENU`, `AppBar.ACTION_BUTTON`, `AppBar.OVERFLOW_MENU`: Action triggers.

### 3. Navigation (Category ID: `NAVIGATION`)
App-level navigation rails, tabs, and backstack:
- `Navigation.BOTTOM_NAVIGATION`: Bottom navigation bar integrating `NavigationManager`.
- `Navigation.NAVIGATION_DRAWER`: Side sliding navigation drawer.
- `Navigation.NAVIGATION_RAIL`: Vertical tablet navigation strip.
- `Navigation.TAB_NAVIGATION`, `Navigation.TAB_LAYOUT`: Tabbed content switchers.
- `Navigation.NAVIGATION_CONTROLLER`: Intent and fragment router.
- `Navigation.NAVIGATION_STACK`: Screen backstack history.
- `Navigation.BACK_NAVIGATION`: System back button dispatcher.
- `Navigation.NAVIGATION_BADGE`, `Navigation.NAVIGATION_INDICATOR`: Visual badges.

### 4. Content (Category ID: `CONTENT`)
Main view containers and layout scaffolding:
- `Content.ROOT_CONTAINER`: Base layout envelope.
- `Content.CONTENT_CONTAINER`: Injected consumer layout container.
- `Content.FRAME_CONTAINER`, `Content.LINEAR_CONTAINER`: Structural layouts.
- `Content.SCROLL_CONTAINER`, `Content.NESTED_SCROLL_CONTAINER`: Scrollable hosts.
- `Content.FRAGMENT_CONTAINER`: Android Fragment host.
- `Content.CUSTOM_VIEW_CONTAINER`: External view host.
- `Content.SAFE_AREA_CONTAINER`: Inset-padded view host.
- `Content.WEB_CONTENT_CONTAINER`: NativeRuntimeEngine WebView host.

### 5. Dialog (Category ID: `DIALOG`)
Modal dialogs, bottom sheets, and prompts:
- `Dialog.ALERT_DIALOG`: Standard alert dialog integrating `DialogManager`.
- `Dialog.CONFIRMATION_DIALOG`: Positive/negative action confirmation.
- `Dialog.CUSTOM_DIALOG`: Custom layout modal dialog.
- `Dialog.SINGLE_CHOICE_DIALOG`, `Dialog.MULTI_CHOICE_DIALOG`: Radio/checkbox dialogs.
- `Dialog.BOTTOM_SHEET_DIALOG`: Modal bottom sheet.
- `Dialog.CUSTOM_BOTTOM_SHEET`: Custom layout sheet integrating `CustomBottomSheet`.
- `Dialog.ACTION_SHEET`: iOS-style action sheet integrating `NativeActionSheet`.
- `Dialog.FULL_SCREEN_DIALOG`: Modal full-screen dialog.
- `Dialog.PERMISSION_DIALOG`: Runtime Android permission rationale prompt.

### 6. Feedback (Category ID: `FEEDBACK`)
User notifications, progress bars, and states:
- `Feedback.TOAST`: Native toast notification integrating `NativeToast`.
- `Feedback.SNACKBAR`: Actionable floating snackbar message.
- `Feedback.LOADING_INDICATOR`: Centered spinner integrating `LoadingIndicator`.
- `Feedback.PROGRESS_INDICATOR`, `Feedback.LINEAR_PROGRESS`, `Feedback.CIRCULAR_PROGRESS`: Progress indicators.
- `Feedback.SUCCESS_MESSAGE`, `Feedback.ERROR_MESSAGE`, `Feedback.WARNING_MESSAGE`: Visual banners.
- `Feedback.EMPTY_STATE`, `Feedback.ERROR_STATE`: Full-screen zero-data/error screens.

### 7. Overlay (Category ID: `OVERLAY`)
Floating controls, tooltips, and modal layers:
- `Overlay.LOADING_OVERLAY`: Fullscreen touch blocker integrating `LoadingOverlayManager`.
- `Overlay.BLOCKING_OVERLAY`: Transparent touch interceptor.
- `Overlay.FLOATING_ACTION_BUTTON`, `Overlay.EXTENDED_FLOATING_ACTION_BUTTON`: FABs.
- `Overlay.FLOATING_MENU`: Expandable speed dial menu.
- `Overlay.POPUP_WINDOW`, `Overlay.DROPDOWN_POPUP`: Anchored popup menus.
- `Overlay.TOOLTIP`: Contextual help bubble.
- `Overlay.CUSTOM_OVERLAY`: Coordinate-based custom overlay.
- `Overlay.SPOTLIGHT_OVERLAY`: Onboarding feature highlight coach mark.

### 8. Input (Category ID: `INPUT`)
Form inputs, selectors, and pickers:
- `Input.TEXT_FIELD`, `Input.PASSWORD_FIELD`, `Input.MULTILINE_TEXT_FIELD`, `Input.SEARCH_INPUT`: Text inputs.
- `Input.CHECKBOX`, `Input.RADIO_BUTTON`, `Input.RADIO_GROUP`, `Input.SWITCH`: Binary toggles.
- `Input.SLIDER`, `Input.RANGE_SLIDER`: Value sliders.
- `Input.DROPDOWN`: Spinner selector.
- `Input.DATE_PICKER`, `Input.TIME_PICKER`: Temporal pickers.
- `Input.NUMBER_INPUT`: Stepper number input.

### 9. WebHost (Category ID: `WEB_HOST`)
Sandboxed WebView container and bridge synchronization:
- `WebHost.WEB_VIEW_CONTAINER`: WebView host integrating `native-runtime-engine`.
- `WebHost.WEB_LOADING_VIEW`: HTML loading shimmer.
- `WebHost.WEB_ERROR_VIEW`: HTTP/SSL error screen.
- `WebHost.WEB_PERMISSION_VIEW`: Geolocation/camera prompt.
- `WebHost.WEB_NAVIGATION_CONTROLS`: Back/Forward/Reload buttons.
- `WebHost.WEB_PROGRESS_INDICATOR`: Top progress bar.
- `WebHost.WEB_OFFLINE_VIEW`: Offline fallback screen.

### 10. Theme (Category ID: `THEME`)
Cross-cutting design token subsystem:
- `Theme.THEME_CONTROLLER`: Dynamic theme engine integrating `native-theme-engine`.
- `Theme.THEME_MODE`: Light / Dark / Follow System mode.
- `Theme.COLOR_SCHEME`: Active color palette.
- `Theme.TYPOGRAPHY`: Font sizes and weights.
- `Theme.SHAPE_STYLE`: Corner radiuses and elevation.
- `Theme.COMPONENT_STYLE`: Widget-specific overrides.
- `Theme.SYSTEM_BAR_THEME`: Status/Navigation bar contrast.
- `Theme.DYNAMIC_THEME`: Dynamic wallpaper theming.
- `Theme.THEME_TRANSITION`: Animated theme transitions.

### 11. Lifecycle (Category ID: `LIFECYCLE`)
Cross-cutting state preservation and view restoration:
- `Lifecycle.UI_STATE_CONTROLLER`: Master UI coordinator.
- `Lifecycle.COMPONENT_LIFECYCLE`: Component attachment/detachment events.
- `Lifecycle.CONFIGURATION_CHANGE_HANDLER`: Orientation and locale shifts.
- `Lifecycle.VISIBILITY_CONTROLLER`: Visibility coordination.
- `Lifecycle.STATE_RESTORATION`: Bundle save and restore pipeline.
- `Lifecycle.SCREEN_STATE_CONTROLLER`: Window focus and display events.

### 12. Animation (Category ID: `ANIMATION`)
Cross-cutting transition and motion choreography:
- `Animation.TRANSITION_ANIMATION`: Layout container transforms.
- `Animation.ENTER_ANIMATION`, `Animation.EXIT_ANIMATION`: Component enter/exit effects.
- `Animation.SHARED_ELEMENT_TRANSITION`: Shared hero transitions.
- `Animation.LOADING_ANIMATION`: Shimmer loading animators.
- `Animation.COMPONENT_ANIMATION`: Property animators.
- `Animation.MOTION_CONTROLLER`: Spring physics and interpolation.
