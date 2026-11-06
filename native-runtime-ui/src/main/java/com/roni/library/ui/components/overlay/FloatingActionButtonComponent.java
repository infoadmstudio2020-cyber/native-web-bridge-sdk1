package com.roni.library.ui.components.overlay;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.roni.library.contracts.ui.Overlay;
import com.roni.library.contracts.ui.IUIComponentDescriptor;
import com.roni.library.ui.base.AbstractUIComponent;
import com.roni.library.ui.registry.UIComponentDescriptor;

/**
 * Concrete implementation for FloatingActionButton component in category Overlay.
 */
public class FloatingActionButtonComponent extends AbstractUIComponent {

    public static IUIComponentDescriptor createDescriptor() {
        return new UIComponentDescriptor(
                Overlay.FLOATING_ACTION_BUTTON,
                "FloatingActionButton",
                "Native implementation of FloatingActionButton component",
                true,
                50
        );
    }

    public FloatingActionButtonComponent(@NonNull IUIComponentDescriptor descriptor) {
        super(descriptor);
    }

    public FloatingActionButtonComponent() {
        this(createDescriptor());
    }

    @Nullable
    @Override
    protected View onCreateView(@NonNull Context context, @Nullable ViewGroup parent) {
        View componentView = createSpecificView(context, parent);
        if (componentView == null) {
            TextView tv = new TextView(context);
            tv.setText("FloatingActionButton");
            tv.setTextColor(Color.DKGRAY);
            tv.setPadding(16, 8, 16, 8);
            componentView = tv;
        }
        return componentView;
    }

    @Nullable
    protected View createSpecificView(@NonNull Context context, @Nullable ViewGroup parent) {
        // Specific view factory
        TextView tv = new TextView(context);
        tv.setText("FloatingActionButton Component");
        tv.setTag("Overlay.FLOATING_ACTION_BUTTON");
        return tv;
    }
}
