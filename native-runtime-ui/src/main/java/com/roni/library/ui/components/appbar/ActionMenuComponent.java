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
 * Concrete implementation for ActionMenu component in category AppBar.
 */
public class ActionMenuComponent extends AbstractUIComponent {

    public static IUIComponentDescriptor createDescriptor() {
        return new UIComponentDescriptor(
                AppBar.ACTION_MENU,
                "ActionMenu",
                "Native implementation of ActionMenu component",
                true,
                50
        );
    }

    public ActionMenuComponent(@NonNull IUIComponentDescriptor descriptor) {
        super(descriptor);
    }

    public ActionMenuComponent() {
        this(createDescriptor());
    }

    @Nullable
    @Override
    protected View onCreateView(@NonNull Context context, @Nullable ViewGroup parent) {
        View componentView = createSpecificView(context, parent);
        if (componentView == null) {
            TextView tv = new TextView(context);
            tv.setText("ActionMenu");
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
        tv.setText("ActionMenu Component");
        tv.setTag("AppBar.ACTION_MENU");
        return tv;
    }
}
