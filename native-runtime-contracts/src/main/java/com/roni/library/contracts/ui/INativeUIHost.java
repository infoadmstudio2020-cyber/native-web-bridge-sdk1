package com.roni.library.contracts.ui;

import android.content.Context;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface INativeUIHost {
    @NonNull
    IUIComponentRegistry getRegistry();
    @NonNull
    IUIConfigurationManager getConfigurationManager();
    @NonNull
    IUICompositionManager getCompositionManager();
    @NonNull
    IUIVisibilityManager getVisibilityManager();
    @NonNull
    IUIStateManager getStateManager();
    @NonNull
    IUIEventDispatcher getEventDispatcher();
    @NonNull
    IUIRenderer getRenderer();
    @Nullable
    View getRootView();
    void reset();
}
