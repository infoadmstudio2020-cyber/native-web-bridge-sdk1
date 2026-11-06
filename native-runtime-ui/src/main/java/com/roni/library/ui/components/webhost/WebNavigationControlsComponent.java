package com.roni.library.ui.components.webhost;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.roni.library.contracts.ui.WebHost;
import com.roni.library.contracts.ui.IUIComponentDescriptor;
import com.roni.library.ui.base.AbstractUIComponent;
import com.roni.library.ui.registry.UIComponentDescriptor;

/**
 * Concrete implementation for WebNavigationControls component in category WebHost.
 */
public class WebNavigationControlsComponent extends AbstractUIComponent {

    public static IUIComponentDescriptor createDescriptor() {
        return new UIComponentDescriptor(
                WebHost.WEB_NAVIGATION_CONTROLS,
                "WebNavigationControls",
                "Native implementation of WebNavigationControls component",
                true,
                50
        );
    }

    public WebNavigationControlsComponent(@NonNull IUIComponentDescriptor descriptor) {
        super(descriptor);
    }

    public WebNavigationControlsComponent() {
        this(createDescriptor());
    }

    @Nullable
    @Override
    protected View onCreateView(@NonNull Context context, @Nullable ViewGroup parent) {
        View componentView = createSpecificView(context, parent);
        if (componentView == null) {
            TextView tv = new TextView(context);
            tv.setText("WebNavigationControls");
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
        tv.setText("WebNavigationControls Component");
        tv.setTag("WebHost.WEB_NAVIGATION_CONTROLS");
        return tv;
    }
}
