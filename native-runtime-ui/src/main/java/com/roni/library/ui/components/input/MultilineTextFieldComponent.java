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
 * Concrete implementation for MultilineTextField component in category Input.
 */
public class MultilineTextFieldComponent extends AbstractUIComponent {

    public static IUIComponentDescriptor createDescriptor() {
        return new UIComponentDescriptor(
                Input.MULTILINE_TEXT_FIELD,
                "MultilineTextField",
                "Native implementation of MultilineTextField component",
                true,
                50
        );
    }

    public MultilineTextFieldComponent(@NonNull IUIComponentDescriptor descriptor) {
        super(descriptor);
    }

    public MultilineTextFieldComponent() {
        this(createDescriptor());
    }

    @Nullable
    @Override
    protected View onCreateView(@NonNull Context context, @Nullable ViewGroup parent) {
        View componentView = createSpecificView(context, parent);
        if (componentView == null) {
            TextView tv = new TextView(context);
            tv.setText("MultilineTextField");
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
        tv.setText("MultilineTextField Component");
        tv.setTag("Input.MULTILINE_TEXT_FIELD");
        return tv;
    }
}
