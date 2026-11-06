package com.roni.library.ui;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.roni.library.contracts.ui.IUIComponentRegistry;
import com.roni.library.contracts.ui.IUICompositionManager;
import com.roni.library.contracts.ui.IUIConfigurationManager;
import com.roni.library.contracts.ui.IUIEventDispatcher;
import com.roni.library.contracts.ui.IUIRenderer;
import com.roni.library.contracts.ui.IUIStateManager;
import com.roni.library.contracts.ui.IUIVisibilityManager;
import com.roni.library.contracts.ui.UICategory;
import com.roni.library.contracts.ui.UIComponentType;
import com.roni.library.ui.handler.NativeUICommandHandler;
import com.roni.library.ui.host.NativeUIHost;
import com.roni.library.ui.registry.StandardUIComponentRegistryInitializer;
import com.roni.library.ui.registry.UIComponentRegistry;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Thread-safe singleton entry point for NativeRuntimeUI adhering to registry-driven architecture.
 */
public class NativeRuntimeUI {

    private static volatile NativeRuntimeUI instance;

    private final AtomicBoolean initialized = new AtomicBoolean(false);
    private NativeRuntimeUIConfig config;
    private NativeUIHost uiHost;
    private NativeUICommandHandler commandHandler;
    private NativeToast toast;
    private LoadingIndicator loadingIndicator;
    private CustomBottomSheet bottomSheet;
    private NativeActionSheet actionSheet;

    private NativeRuntimeUI() {}

    public static NativeRuntimeUI getInstance() {
        if (instance == null) {
            synchronized (NativeRuntimeUI.class) {
                if (instance == null) {
                    instance = new NativeRuntimeUI();
                }
            }
        }
        return instance;
    }

    public synchronized NativeRuntimeUIResult<Boolean> init(Context context, NativeRuntimeUIConfig config) {
        if (initialized.get()) {
            return NativeRuntimeUIResult.success(true);
        }

        this.config = config != null ? config : NativeRuntimeUIConfig.defaultConfiguration();
        this.uiHost = new NativeUIHost();

        // 1. Initialize component registry
        if (this.uiHost.getRegistry() instanceof UIComponentRegistry) {
            StandardUIComponentRegistryInitializer.initializeDefaults((UIComponentRegistry) this.uiHost.getRegistry());
        }

        // 2. Synchronize configuration
        IUIConfigurationManager cfgManager = this.uiHost.getConfigurationManager();
        cfgManager.setGlobalDefault(this.config.getGlobalDefault());
        for (Map.Entry<UICategory, Boolean> entry : this.config.getCategoryDefaults().entrySet()) {
            cfgManager.setCategoryDefault(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<UIComponentType, Boolean> entry : this.config.getComponentOverrides().entrySet()) {
            cfgManager.setComponentOverride(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<UIComponentType, Map<String, Object>> entry : this.config.getComponentProperties().entrySet()) {
            for (Map.Entry<String, Object> prop : entry.getValue().entrySet()) {
                cfgManager.setComponentProperty(entry.getKey(), prop.getKey(), prop.getValue());
            }
        }

        this.commandHandler = new NativeUICommandHandler(this.uiHost);
        this.toast = new NativeToast();
        this.loadingIndicator = new LoadingIndicator();
        this.bottomSheet = new CustomBottomSheet();
        this.actionSheet = new NativeActionSheet();

        this.initialized.set(true);
        return NativeRuntimeUIResult.success(true);
    }

    public boolean isInitialized() {
        return initialized.get();
    }

    public NativeRuntimeUIConfig getConfig() {
        return config;
    }

    public NativeUIHost getUiHost() {
        checkInitialized();
        return uiHost;
    }

    public IUIComponentRegistry getRegistry() {
        return getUiHost().getRegistry();
    }

    public IUIConfigurationManager getConfigurationManager() {
        return getUiHost().getConfigurationManager();
    }

    public IUICompositionManager getCompositionManager() {
        return getUiHost().getCompositionManager();
    }

    public IUIVisibilityManager getVisibilityManager() {
        return getUiHost().getVisibilityManager();
    }

    public IUIStateManager getStateManager() {
        return getUiHost().getStateManager();
    }

    public IUIEventDispatcher getEventDispatcher() {
        return getUiHost().getEventDispatcher();
    }

    public IUIRenderer getRenderer() {
        return getUiHost().getRenderer();
    }

    public NativeUICommandHandler getCommandHandler() {
        checkInitialized();
        return commandHandler;
    }

    public NativeToast getToast() {
        checkInitialized();
        return toast;
    }

    public LoadingIndicator getLoadingIndicator() {
        checkInitialized();
        return loadingIndicator;
    }

    public CustomBottomSheet getBottomSheet() {
        checkInitialized();
        return bottomSheet;
    }

    public NativeActionSheet getActionSheet() {
        checkInitialized();
        return actionSheet;
    }

    public synchronized void release() {
        if (uiHost != null) {
            uiHost.reset();
            uiHost = null;
        }
        if (toast != null) {
            toast.dismiss();
            toast = null;
        }
        if (loadingIndicator != null) {
            loadingIndicator.hide();
            loadingIndicator = null;
        }
        if (bottomSheet != null) {
            bottomSheet.dismiss();
            bottomSheet = null;
        }
        if (actionSheet != null) {
            actionSheet.clear();
            actionSheet = null;
        }
        commandHandler = null;
        config = null;
        initialized.set(false);
        instance = null;
    }

    private void checkInitialized() {
        if (!initialized.get()) {
            init(null, NativeRuntimeUIConfig.defaultConfiguration());
        }
    }
}
