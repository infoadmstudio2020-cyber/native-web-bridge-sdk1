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
 * Concrete implementation for NestedScrollContainer component in category Content.
 */
public class NestedScrollContainerComponent extends AbstractUIComponent {

    public static IUIComponentDescriptor createDescriptor() {
        return new UIComponentDescriptor(
                Content.NESTED_SCROLL_CONTAINER,
                "NestedScrollContainer",
                "Native implementation of NestedScrollContainer component",
                true,
                50
        );
    }

    public NestedScrollContainerComponent(@NonNull IUIComponentDescriptor descriptor) {
        super(descriptor);
    }

    public NestedScrollContainerComponent() {
        this(createDescriptor());
    }

    @Nullable
    @Override
    protected View onCreateView(@NonNull Context context, @Nullable ViewGroup parent) {
        View componentView = createSpecificView(context, parent);
        if (componentView == null) {
            TextView tv = new TextView(context);
            tv.setText("NestedScrollContainer");
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
        tv.setText("NestedScrollContainer Component");
        tv.setTag("Content.NESTED_SCROLL_CONTAINER");
        return tv;
    }
}
