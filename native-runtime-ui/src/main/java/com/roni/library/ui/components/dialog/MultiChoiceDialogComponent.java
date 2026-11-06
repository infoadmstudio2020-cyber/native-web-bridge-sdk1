package com.roni.library.ui.components.dialog;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.roni.library.contracts.ui.Dialog;
import com.roni.library.contracts.ui.IUIComponentDescriptor;
import com.roni.library.ui.base.AbstractUIComponent;
import com.roni.library.ui.registry.UIComponentDescriptor;

/**
 * Concrete implementation for MultiChoiceDialog component in category Dialog.
 */
public class MultiChoiceDialogComponent extends AbstractUIComponent {

    public static IUIComponentDescriptor createDescriptor() {
        return new UIComponentDescriptor(
                Dialog.MULTI_CHOICE_DIALOG,
                "MultiChoiceDialog",
                "Native implementation of MultiChoiceDialog component",
                true,
                50
        );
    }

    public MultiChoiceDialogComponent(@NonNull IUIComponentDescriptor descriptor) {
        super(descriptor);
    }

    public MultiChoiceDialogComponent() {
        this(createDescriptor());
    }

    @Nullable
    @Override
    protected View onCreateView(@NonNull Context context, @Nullable ViewGroup parent) {
        View componentView = createSpecificView(context, parent);
        if (componentView == null) {
            TextView tv = new TextView(context);
            tv.setText("MultiChoiceDialog");
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
        tv.setText("MultiChoiceDialog Component");
        tv.setTag("Dialog.MULTI_CHOICE_DIALOG");
        return tv;
    }
}
