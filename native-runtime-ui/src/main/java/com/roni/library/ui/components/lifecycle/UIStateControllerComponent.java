package com.roni.library.ui.components.lifecycle;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.roni.library.contracts.ui.Lifecycle;
import com.roni.library.contracts.ui.IUIComponentDescriptor;
import com.roni.library.ui.base.AbstractUIComponent;
import com.roni.library.ui.registry.UIComponentDescriptor;

/**
 * Concrete implementation for UIStateController component in category Lifecycle.
 */
public class UIStateControllerComponent extends AbstractUIComponent {

    public static IUIComponentDescriptor createDescriptor() {
        return new UIComponentDescriptor(
                Lifecycle.UI_STATE_CONTROLLER,
                "UIStateController",
                "Native implementation of UIStateController component",
                true,
                50
        );
    }

    public UIStateControllerComponent(@NonNull IUIComponentDescriptor descriptor) {
        super(descriptor);
    }

    public UIStateControllerComponent() {
        this(createDescriptor());
    }

    @Nullable
    @Override
    protected View onCreateView(@NonNull Context context, @Nullable ViewGroup parent) {
        View componentView = createSpecificView(context, parent);
        if (componentView == null) {
            TextView tv = new TextView(context);
            tv.setText("UIStateController");
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
        tv.setText("UIStateController Component");
        tv.setTag("Lifecycle.UI_STATE_CONTROLLER");
        return tv;
    }
}
