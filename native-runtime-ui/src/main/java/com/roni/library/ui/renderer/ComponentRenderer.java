package com.roni.library.ui.renderer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.roni.library.contracts.ui.IComponentRenderer;
import com.roni.library.contracts.ui.IUIComponent;

public class ComponentRenderer implements IComponentRenderer {

    public ComponentRenderer() {}

    @Nullable
    @Override
    public View renderComponent(@NonNull Context context, @Nullable ViewGroup parent, @NonNull IUIComponent component) {
        if (!component.isEnabled()) {
            return null;
        }
        return component.render(context, parent);
    }
}
