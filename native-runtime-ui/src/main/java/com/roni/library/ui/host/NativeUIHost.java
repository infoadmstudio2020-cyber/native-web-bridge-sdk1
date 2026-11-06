package com.roni.library.ui.host;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.roni.library.contracts.ui.INativeUIHost;
import com.roni.library.contracts.ui.IUIComponentRegistry;
import com.roni.library.contracts.ui.IUICompositionManager;
import com.roni.library.contracts.ui.IUIConfigurationManager;
import com.roni.library.contracts.ui.IUIEventDispatcher;
import com.roni.library.contracts.ui.IUIRenderer;
import com.roni.library.contracts.ui.IUIStateManager;
import com.roni.library.contracts.ui.IUIVisibilityManager;
import com.roni.library.ui.composition.UICompositionManager;
import com.roni.library.ui.config.UIConfigurationManager;
import com.roni.library.ui.dialog.DialogManager;
import com.roni.library.ui.event.UIEventDispatcher;
import com.roni.library.ui.navigation.NavigationManager;
import com.roni.library.ui.overlay.LoadingOverlayManager;
import com.roni.library.ui.registry.UIComponentRegistry;
import com.roni.library.ui.renderer.UIRenderer;
import com.roni.library.ui.state.UIStateManager;
import com.roni.library.ui.toolbar.ToolbarManager;
import com.roni.library.ui.view.NativeUIRootView;
import com.roni.library.ui.visibility.UIVisibilityManager;

/**
 * NativeUIHost aggregates the complete UI Architecture and integrates existing managers.
 */
public class NativeUIHost implements INativeUIHost {

    private final IUIComponentRegistry registry;
    private final IUIConfigurationManager configurationManager;
    private final IUICompositionManager compositionManager;
    private final IUIVisibilityManager visibilityManager;
    private final IUIStateManager stateManager;
    private final IUIEventDispatcher eventDispatcher;
    private final IUIRenderer renderer;

    // Existing Managers integrated without duplication
    private final DialogManager dialogManager;
    private final LoadingOverlayManager loadingOverlayManager;
    private final NavigationManager navigationManager;
    private final ToolbarManager toolbarManager;

    private NativeUIRootView rootView;

    public NativeUIHost() {
        this.registry = new UIComponentRegistry();
        this.configurationManager = new UIConfigurationManager();
        this.compositionManager = new UICompositionManager();
        this.visibilityManager = new UIVisibilityManager(this.registry);
        this.stateManager = new UIStateManager();
        this.eventDispatcher = new UIEventDispatcher();
        this.renderer = new UIRenderer();

        this.dialogManager = new DialogManager();
        this.loadingOverlayManager = new LoadingOverlayManager();
        this.navigationManager = new NavigationManager();
        this.toolbarManager = new ToolbarManager();
    }

    @NonNull
    @Override
    public IUIComponentRegistry getRegistry() { return registry; }

    @NonNull
    @Override
    public IUIConfigurationManager getConfigurationManager() { return configurationManager; }

    @NonNull
    @Override
    public IUICompositionManager getCompositionManager() { return compositionManager; }

    @NonNull
    @Override
    public IUIVisibilityManager getVisibilityManager() { return visibilityManager; }

    @NonNull
    @Override
    public IUIStateManager getStateManager() { return stateManager; }

    @NonNull
    @Override
    public IUIEventDispatcher getEventDispatcher() { return eventDispatcher; }

    @NonNull
    @Override
    public IUIRenderer getRenderer() { return renderer; }

    @NonNull
    public DialogManager getDialogManager() { return dialogManager; }

    @NonNull
    public LoadingOverlayManager getLoadingOverlayManager() { return loadingOverlayManager; }

    @NonNull
    public NavigationManager getNavigationManager() { return navigationManager; }

    @NonNull
    public ToolbarManager getToolbarManager() { return toolbarManager; }

    @Nullable
    @Override
    public View getRootView() { return rootView; }

    @NonNull
    public NativeUIRootView attachRootView(@NonNull Context context) {
        if (rootView == null) {
            rootView = (NativeUIRootView) renderer.render(context, null, registry, configurationManager, compositionManager);
        }
        return rootView;
    }

    @Override
    public void reset() {
        if (rootView != null) {
            rootView.removeAllViews();
            rootView = null;
        }
        if (registry instanceof UIComponentRegistry) {
            ((UIComponentRegistry) registry).clear();
        }
        if (configurationManager instanceof UIConfigurationManager) {
            ((UIConfigurationManager) configurationManager).clear();
        }
        if (compositionManager instanceof UICompositionManager) {
            ((UICompositionManager) compositionManager).clear();
        }
        if (visibilityManager instanceof UIVisibilityManager) {
            ((UIVisibilityManager) visibilityManager).clear();
        }
        stateManager.reset();
        eventDispatcher.clearListeners();
    }
}
