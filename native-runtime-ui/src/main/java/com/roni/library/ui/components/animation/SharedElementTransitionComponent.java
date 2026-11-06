package com.roni.library.ui.components.animation;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.roni.library.contracts.ui.Animation;
import com.roni.library.contracts.ui.IUIComponentDescriptor;
import com.roni.library.ui.base.AbstractUIComponent;
import com.roni.library.ui.registry.UIComponentDescriptor;

/**
 * Concrete implementation for SharedElementTransition component in category Animation.
 */
public class SharedElementTransitionComponent extends AbstractUIComponent {

    public static IUIComponentDescriptor createDescriptor() {
        return new UIComponentDescriptor(
                Animation.SHARED_ELEMENT_TRANSITION,
                "SharedElementTransition",
                "Native implementation of SharedElementTransition component",
                true,
                50
        );
    }

    public SharedElementTransitionComponent(@NonNull IUIComponentDescriptor descriptor) {
        super(descriptor);
    }

    public SharedElementTransitionComponent() {
        this(createDescriptor());
    }

    @Nullable
    @Override
    protected View onCreateView(@NonNull Context context, @Nullable ViewGroup parent) {
        View componentView = createSpecificView(context, parent);
        if (componentView == null) {
            TextView tv = new TextView(context);
            tv.setText("SharedElementTransition");
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
        tv.setText("SharedElementTransition Component");
        tv.setTag("Animation.SHARED_ELEMENT_TRANSITION");
        return tv;
    }
}
