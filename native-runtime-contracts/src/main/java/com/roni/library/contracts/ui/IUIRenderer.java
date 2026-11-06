package com.roni.library.contracts.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface IUIRenderer {
    @NonNull
    View render(@NonNull Context context,
                @Nullable ViewGroup parent,
                @NonNull IUIComponentRegistry registry,
                @NonNull IUIConfigurationManager configManager,
                @NonNull IUICompositionManager compositionManager);
}
