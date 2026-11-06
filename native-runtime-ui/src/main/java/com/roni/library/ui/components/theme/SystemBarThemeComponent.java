package com.roni.library.ui.components.theme;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.roni.library.contracts.ui.Theme;
import com.roni.library.contracts.ui.IUIComponentDescriptor;
import com.roni.library.ui.base.AbstractUIComponent;
import com.roni.library.ui.registry.UIComponentDescriptor;

/**
 * Concrete implementation for SystemBarTheme component in category Theme.
 */
public class SystemBarThemeComponent extends AbstractUIComponent {

    public static IUIComponentDescriptor createDescriptor() {
        return new UIComponentDescriptor(
                Theme.SYSTEM_BAR_THEME,
                "SystemBarTheme",
                "Native implementation of SystemBarTheme component",
                true,
                50
        );
    }

    public SystemBarThemeComponent(@NonNull IUIComponentDescriptor descriptor) {
        super(descriptor);
    }

    public SystemBarThemeComponent() {
        this(createDescriptor());
    }

    @Nullable
    @Override
    protected View onCreateView(@NonNull Context context, @Nullable ViewGroup parent) {
        View componentView = createSpecificView(context, parent);
        if (componentView == null) {
            TextView tv = new TextView(context);
            tv.setText("SystemBarTheme");
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
        tv.setText("SystemBarTheme Component");
        tv.setTag("Theme.SYSTEM_BAR_THEME");
        return tv;
    }
}
