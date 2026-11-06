package com.roni.library.ui.components.content;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.roni.library.contracts.ui.Content;
import com.roni.library.contracts.ui.IUIComponentDescriptor;
import com.roni.library.ui.base.AbstractUIComponent;
import com.roni.library.ui.registry.UIComponentDescriptor;

/**
 * Concrete implementation for SafeAreaContainer component in category Content.
 */
public class SafeAreaContainerComponent extends AbstractUIComponent {

    public static IUIComponentDescriptor createDescriptor() {
        return new UIComponentDescriptor(
                Content.SAFE_AREA_CONTAINER,
                "SafeAreaContainer",
                "Native implementation of SafeAreaContainer component",
                true,
                50
        );
    }

    public SafeAreaContainerComponent(@NonNull IUIComponentDescriptor descriptor) {
        super(descriptor);
    }

    public SafeAreaContainerComponent() {
        this(createDescriptor());
    }

    @Nullable
    @Override
    protected View onCreateView(@NonNull Context context, @Nullable ViewGroup parent) {
        View componentView = createSpecificView(context, parent);
        if (componentView == null) {
            TextView tv = new TextView(context);
            tv.setText("SafeAreaContainer");
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
        tv.setText("SafeAreaContainer Component");
        tv.setTag("Content.SAFE_AREA_CONTAINER");
        return tv;
    }
}
