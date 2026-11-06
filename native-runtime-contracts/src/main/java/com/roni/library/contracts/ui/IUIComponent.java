package com.roni.library.contracts.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface IUIComponent {
    @NonNull
    UIComponentType getType();

    @NonNull
    IUIComponentDescriptor getDescriptor();

    @NonNull
    IUIComponentController getController();

    @NonNull
    ComponentLifecycleState getLifecycleState();

    boolean isEnabled();

    void setEnabled(boolean enabled);

    boolean isVisible();

    void setVisible(boolean visible);

    @Nullable
    View render(@NonNull Context context, @Nullable ViewGroup parent);

    @Nullable
    View getView();

    @NonNull
    UIState getState();

    void destroy();
}
