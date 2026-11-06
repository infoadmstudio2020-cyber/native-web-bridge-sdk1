package com.roni.library.contracts.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface IComponentRenderer {
    @Nullable
    View renderComponent(@NonNull Context context, @Nullable ViewGroup parent, @NonNull IUIComponent component);
}
