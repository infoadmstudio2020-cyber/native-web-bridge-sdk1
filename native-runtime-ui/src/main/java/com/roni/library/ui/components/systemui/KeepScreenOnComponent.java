package com.roni.library.ui.components.systemui;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.roni.library.contracts.ui.SystemUI;
import com.roni.library.contracts.ui.IUIComponentDescriptor;
import com.roni.library.ui.base.AbstractUIComponent;
import com.roni.library.ui.registry.UIComponentDescriptor;

/**
 * Concrete implementation for KeepScreenOn component in category SystemUI.
 */
public class KeepScreenOnComponent extends AbstractUIComponent {

    public static IUIComponentDescriptor createDescriptor() {
        return new UIComponentDescriptor(
                SystemUI.KEEP_SCREEN_ON,
                "KeepScreenOn",
                "Native implementation of KeepScreenOn component",
                true,
                50
        );
    }

    public KeepScreenOnComponent(@NonNull IUIComponentDescriptor descriptor) {
        super(descriptor);
    }

    public KeepScreenOnComponent() {
        this(createDescriptor());
    }

    @Nullable
    @Override
    protected View onCreateView(@NonNull Context context, @Nullable ViewGroup parent) {
        View componentView = createSpecificView(context, parent);
        if (componentView == null) {
            TextView tv = new TextView(context);
            tv.setText("KeepScreenOn");
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
        tv.setText("KeepScreenOn Component");
        tv.setTag("SystemUI.KEEP_SCREEN_ON");
        return tv;
    }
}
