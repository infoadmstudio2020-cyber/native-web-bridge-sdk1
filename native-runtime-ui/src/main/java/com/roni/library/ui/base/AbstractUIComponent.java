package com.roni.library.ui.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.roni.library.contracts.ui.ComponentLifecycleState;
import com.roni.library.contracts.ui.IUIComponent;
import com.roni.library.contracts.ui.IUIComponentController;
import com.roni.library.contracts.ui.IUIComponentDescriptor;
import com.roni.library.contracts.ui.UIComponentType;
import com.roni.library.contracts.ui.UIState;
import com.roni.library.ui.controller.UIComponentController;
import java.util.Objects;

public abstract class AbstractUIComponent implements IUIComponent {

    protected final IUIComponentDescriptor descriptor;
    protected final IUIComponentController controller;
    protected final UIState state;
    protected volatile ComponentLifecycleState lifecycleState = ComponentLifecycleState.UNINITIALIZED;
    protected volatile boolean enabled = true;
    protected volatile boolean visible = true;
    protected View view;

    public AbstractUIComponent(@NonNull IUIComponentDescriptor descriptor,
                               @Nullable IUIComponentController controller) {
        this.descriptor = Objects.requireNonNull(descriptor, "descriptor cannot be null");
        this.controller = controller != null ? controller : new UIComponentController();
        this.state = new UIState(descriptor.getType().getQualifiedId());
        this.enabled = descriptor.isEnabledByDefault();
    }

    public AbstractUIComponent(@NonNull IUIComponentDescriptor descriptor) {
        this(descriptor, null);
    }

    @NonNull
    @Override
    public UIComponentType getType() {
        return descriptor.getType();
    }

    @NonNull
    @Override
    public IUIComponentDescriptor getDescriptor() {
        return descriptor;
    }

    @NonNull
    @Override
    public IUIComponentController getController() {
        return controller;
    }

    @NonNull
    @Override
    public ComponentLifecycleState getLifecycleState() {
        return lifecycleState;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
        if (view != null) {
            view.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
        controller.onVisibilityChanged(visible);
        lifecycleState = visible ? ComponentLifecycleState.VISIBLE : ComponentLifecycleState.HIDDEN;
    }

    @Nullable
    @Override
    public View render(@NonNull Context context, @Nullable ViewGroup parent) {
        controller.onAttach(context);
        lifecycleState = ComponentLifecycleState.ATTACHED;

        controller.onInit(state.snapshot());
        lifecycleState = ComponentLifecycleState.INITIALIZED;

        this.view = onCreateView(context, parent);
        if (this.view != null) {
            this.view.setVisibility(visible ? View.VISIBLE : View.GONE);
            controller.onRender(this.view);
            lifecycleState = ComponentLifecycleState.RENDERED;
        }
        return this.view;
    }

    @Nullable
    protected abstract View onCreateView(@NonNull Context context, @Nullable ViewGroup parent);

    @Nullable
    @Override
    public View getView() {
        return view;
    }

    @NonNull
    @Override
    public UIState getState() {
        return state;
    }

    @Override
    public void destroy() {
        if (view != null && view.getParent() instanceof ViewGroup) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        controller.onDestroy();
        lifecycleState = ComponentLifecycleState.DESTROYED;
        view = null;
    }
}
