package com.roni.library.contracts.ui;

import android.content.Context;
import androidx.annotation.NonNull;

public interface IUIComponentProvider {
    @NonNull
    UIComponentType getProvidedType();

    @NonNull
    IUIComponent createComponent(@NonNull Context context, @NonNull IUIComponentDescriptor descriptor);
}
