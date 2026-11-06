package com.roni.library.ui.components.input;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.roni.library.contracts.ui.Input;
import com.roni.library.contracts.ui.IUIComponentDescriptor;
import com.roni.library.ui.base.AbstractUIComponent;
import com.roni.library.ui.registry.UIComponentDescriptor;

/**
 * Concrete implementation for SearchInput component in category Input.
 */
public class SearchInputComponent extends AbstractUIComponent {

    public static IUIComponentDescriptor createDescriptor() {
        return new UIComponentDescriptor(
                Input.SEARCH_INPUT,
                "SearchInput",
                "Native implementation of SearchInput component",
                true,
                50
        );
    }

    public SearchInputComponent(@NonNull IUIComponentDescriptor descriptor) {
        super(descriptor);
    }

    public SearchInputComponent() {
        this(createDescriptor());
    }

    @Nullable
    @Override
    protected View onCreateView(@NonNull Context context, @Nullable ViewGroup parent) {
        View componentView = createSpecificView(context, parent);
        if (componentView == null) {
            TextView tv = new TextView(context);
            tv.setText("SearchInput");
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
        tv.setText("SearchInput Component");
        tv.setTag("Input.SEARCH_INPUT");
        return tv;
    }
}
