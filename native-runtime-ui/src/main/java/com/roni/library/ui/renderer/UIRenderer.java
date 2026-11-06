package com.roni.library.ui.renderer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.roni.library.contracts.ui.IComponentRenderer;
import com.roni.library.contracts.ui.IUIComponent;
import com.roni.library.contracts.ui.IUIComponentDescriptor;
import com.roni.library.contracts.ui.IUIComponentRegistry;
import com.roni.library.contracts.ui.IUICompositionManager;
import com.roni.library.contracts.ui.IUIConfigurationManager;
import com.roni.library.contracts.ui.IUIRenderer;
import com.roni.library.ui.view.NativeUIRootView;
import java.util.ArrayList;
import java.util.List;

public class UIRenderer implements IUIRenderer {

    private final IComponentRenderer componentRenderer;

    public UIRenderer(@NonNull IComponentRenderer componentRenderer) {
        this.componentRenderer = componentRenderer;
    }

    public UIRenderer() {
        this(new ComponentRenderer());
    }

    @NonNull
    @Override
    public View render(@NonNull Context context,
                       @Nullable ViewGroup parent,
                       @NonNull IUIComponentRegistry registry,
                       @NonNull IUIConfigurationManager configManager,
                       @NonNull IUICompositionManager compositionManager) {
        NativeUIRootView rootView;
        if (parent instanceof NativeUIRootView) {
            rootView = (NativeUIRootView) parent;
        } else {
            rootView = new NativeUIRootView(context);
        }

        // Attach category containers
        compositionManager.registerCustomContainer(com.roni.library.contracts.ui.UICategory.APP_BAR, rootView.getAppBarContainer());
        compositionManager.registerCustomContainer(com.roni.library.contracts.ui.UICategory.CONTENT, rootView.getContentContainer());
        compositionManager.registerCustomContainer(com.roni.library.contracts.ui.UICategory.NAVIGATION, rootView.getNavigationContainer());
        compositionManager.registerCustomContainer(com.roni.library.contracts.ui.UICategory.OVERLAY, rootView.getOverlayContainer());

        List<IUIComponent> activeComponents = new ArrayList<>();
        for (IUIComponentDescriptor desc : registry.getAllDescriptors()) {
            boolean enabled = configManager.isComponentEnabled(desc.getType());
            if (enabled) {
                IUIComponent comp = registry.getOrCreateComponent(context, desc.getType());
                comp.setEnabled(true);
                componentRenderer.renderComponent(context, rootView, comp);
                activeComponents.add(comp);
            }
        }

        compositionManager.compose(rootView, activeComponents);
        return rootView;
    }
}
