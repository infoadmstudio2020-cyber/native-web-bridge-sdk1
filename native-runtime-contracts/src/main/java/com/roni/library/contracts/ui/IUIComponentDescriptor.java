package com.roni.library.contracts.ui;

import androidx.annotation.NonNull;
import java.io.Serializable;
import java.util.List;

public interface IUIComponentDescriptor extends Serializable {
    @NonNull
    UIComponentType getType();

    @NonNull
    String getName();

    @NonNull
    String getDescription();

    @NonNull
    UICategory getCategory();

    boolean isEnabledByDefault();

    int getPriority();

    @NonNull
    List<UIComponentType> getDependencies();
}
