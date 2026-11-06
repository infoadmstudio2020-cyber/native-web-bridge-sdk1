package com.roni.library.ui.components.appbar;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.roni.library.contracts.ui.AppBar;
import com.roni.library.contracts.ui.IUIComponentDescriptor;
import com.roni.library.ui.base.AbstractUIComponent;
import com.roni.library.ui.registry.UIComponentDescriptor;

/**
 * Concrete implementation for CenterAlignedTopAppBar component in category AppBar.
 */
public class CenterAlignedTopAppBarComponent extends AbstractUIComponent {

    public static IUIComponentDescriptor createDescriptor() {
        return new UIComponentDescriptor(
                AppBar.CENTER_ALIGNED_TOP_APP_BAR,
                "CenterAlignedTopAppBar",
                "Native implementation of CenterAlignedTopAppBar component",
                true,
                50
        );
    }

    public CenterAlignedTopAppBarComponent(@NonNull IUIComponentDescriptor descriptor) {
        super(descriptor);
    }

    public CenterAlignedTopAppBarComponent() {
        this(createDescriptor());
    }

    @Nullable
    @Override
    protected View onCreateView(@NonNull Context context, @Nullable ViewGroup parent) {
        View componentView = createSpecificView(context, parent);
        if (componentView == null) {
            TextView tv = new TextView(context);
            tv.setText("CenterAlignedTopAppBar");
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
        tv.setText("CenterAlignedTopAppBar Component");
        tv.setTag("AppBar.CENTER_ALIGNED_TOP_APP_BAR");
        return tv;
    }
}
