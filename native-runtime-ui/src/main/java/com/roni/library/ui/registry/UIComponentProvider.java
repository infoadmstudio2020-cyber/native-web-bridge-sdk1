package com.roni.library.ui.registry;

import android.content.Context;
import androidx.annotation.NonNull;
import com.roni.library.contracts.ui.IUIComponent;
import com.roni.library.contracts.ui.IUIComponentDescriptor;
import com.roni.library.contracts.ui.IUIComponentProvider;
import com.roni.library.contracts.ui.UIComponentType;
import java.util.Objects;

public abstract class UIComponentProvider implements IUIComponentProvider {

    private final UIComponentType providedType;

    public UIComponentProvider(@NonNull UIComponentType providedType) {
        this.providedType = Objects.requireNonNull(providedType, "providedType cannot be null");
    }

    @NonNull
    @Override
    public UIComponentType getProvidedType() {
        return providedType;
    }

    @NonNull
    @Override
    public abstract IUIComponent createComponent(@NonNull Context context, @NonNull IUIComponentDescriptor descriptor);
}
