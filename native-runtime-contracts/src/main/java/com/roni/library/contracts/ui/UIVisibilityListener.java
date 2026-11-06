package com.roni.library.contracts.ui;

import androidx.annotation.NonNull;

public interface UIVisibilityListener {
    void onVisibilityChanged(@NonNull UIComponentType componentType, boolean isVisible);
}
