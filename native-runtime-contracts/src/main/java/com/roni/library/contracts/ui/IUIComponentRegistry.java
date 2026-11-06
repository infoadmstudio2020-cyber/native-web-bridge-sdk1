package com.roni.library.contracts.ui;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;

public interface IUIComponentRegistry {
    void registerDescriptor(@NonNull IUIComponentDescriptor descriptor);
    void registerProvider(@NonNull IUIComponentProvider provider);
    @Nullable
    IUIComponentDescriptor getDescriptor(@NonNull UIComponentType type);
    @NonNull
    List<IUIComponentDescriptor> getDescriptorsByCategory(@NonNull UICategory category);
    @NonNull
    List<IUIComponentDescriptor> getAllDescriptors();
    @Nullable
    IUIComponent getComponent(@NonNull UIComponentType type);
    @NonNull
    IUIComponent getOrCreateComponent(@NonNull Context context, @NonNull UIComponentType type);
    boolean isRegistered(@NonNull UIComponentType type);
    int getComponentCount();
}
