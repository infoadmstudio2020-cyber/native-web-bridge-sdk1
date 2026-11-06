# Consumer API & NativeRuntimeActivity Integration Guide

## 1. Overview

The NativeRuntime SDK provides a unified, single consumer entry point through `NativeRuntimeActivity`. Consumers extend `NativeRuntimeActivity` and override `onConfigureUI()` using a fluent DSL.

---

## 2. Target Consumer API Usage

```java
package com.mycompany.app;

import com.roni.library.contracts.ui.AppBar;
import com.roni.library.contracts.ui.Content;
import com.roni.library.contracts.ui.Dialog;
import com.roni.library.contracts.ui.Feedback;
import com.roni.library.contracts.ui.Navigation;
import com.roni.library.contracts.ui.Overlay;
import com.roni.library.contracts.ui.SystemUI;
import com.roni.library.contracts.ui.UICategory;
import com.roni.library.sdk.activity.NativeRuntimeActivity;
import com.roni.library.ui.NativeRuntimeUIConfig;

public class MainActivity extends NativeRuntimeActivity {

    @Override
    protected NativeRuntimeUIConfig onConfigureUI(NativeRuntimeUIConfig.Builder builder) {
        return builder
                .enable(SystemUI.STATUS_BAR)
                .enable(SystemUI.EDGE_TO_EDGE)
                .enable(AppBar.TOOLBAR)
                .enable(Navigation.BOTTOM_NAVIGATION)
                .enable(Content.CONTENT_CONTAINER)
                .enable(Dialog.ALERT_DIALOG)
                .enable(Feedback.TOAST)
                .enable(Overlay.LOADING_OVERLAY)
                .disable(Overlay.FLOATING_MENU)
                .build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Standard setContentView automatically mounts inside NativeUIRootView content container!
        setContentView(R.layout.activity_main);

        // Direct access to aggregated UI managers
        getUIHost().getToolbarManager().setTitle("My Dashboard");
        getUIHost().getNavigationManager().selectTab("tab_home");
    }
}
```

---

## 3. Configuration Priority Resolution

Configuration rules are evaluated using strict hierarchical precedence:

```
+--------------------------------------------------------------+
| Priority 1: Individual Component Override                    |
|             builder.enable(SystemUI.STATUS_BAR)              |
|             builder.disable(Overlay.FLOATING_MENU)           |
+--------------------------------------------------------------+
                               |
                               v (if not specified)
+--------------------------------------------------------------+
| Priority 2: Category Default                                 |
|             builder.enableCategory(UICategory.OVERLAY)       |
+--------------------------------------------------------------+
                               |
                               v (if not specified)
+--------------------------------------------------------------+
| Priority 3: Global Default                                   |
|             builder.setGlobalDefault(false)                  |
+--------------------------------------------------------------+
```

### Examples:
1. `enableCategory(UICategory.OVERLAY)` + `disable(Overlay.FLOATING_MENU)`:
   - `Overlay.LOADING_OVERLAY` -> **Enabled** (via Category Default)
   - `Overlay.FLOATING_MENU` -> **Disabled** (via Component Override)
2. `enable(AppBar.TOOLBAR)` with no Category Default:
   - `AppBar.TOOLBAR` -> **Enabled** (via Component Override)
   - `AppBar.SEARCH_BAR` -> **Disabled** (via Global Default)

---

## 4. WebBridge RPC Integration

Web applications running inside the NativeRuntime WebView container can trigger UI components natively via JSON-RPC:

### RPC Routes:
| Route | Action | Parameters | Description |
|---|---|---|---|
| `ui` | `showAlert` | `{title, message}` | Shows native alert dialog |
| `ui` | `showToast` | `{message}` | Displays native toast |
| `ui` | `showBottomSheet` | `{title}` | Expands custom bottom sheet |
| `ui` | `showLoading` | `{text}` | Displays full-screen loading overlay |
| `ui` | `hideLoading` | `{}` | Dismisses loading overlay |
| `ui` | `setToolbarTitle` | `{title}` | Updates native toolbar title |
| `ui` | `selectTab` | `{tabId}` | Programmatically selects navigation tab |
| `ui` | `isComponentEnabled`| `{category, key}` | Queries component configuration |
| `ui` | `setComponentVisible`| `{category, key, visible}` | Dynamically shows/hides component |
| `ui` | `getComponentCount` | `{}` | Returns total registered components |
