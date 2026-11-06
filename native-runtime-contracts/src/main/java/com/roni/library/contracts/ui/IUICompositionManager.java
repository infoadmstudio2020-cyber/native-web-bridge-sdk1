package com.roni.library.contracts.ui;

import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;

public interface IUICompositionManager {
    void compose(@NonNull ViewGroup rootView, @NonNull List<IUIComponent> components);
    @Nullable
    ViewGroup getContainerForCategory(@NonNull UICategory category);
    void registerCustomContainer(@NonNull UICategory category, @NonNull ViewGroup container);
}
