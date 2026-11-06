package com.roni.library.contracts.ui;

import androidx.annotation.NonNull;

public interface IUIVisibilityManager {
    void setVisible(@NonNull UIComponentType type, boolean visible);
    boolean isVisible(@NonNull UIComponentType type);
    void toggle(@NonNull UIComponentType type);
    void addVisibilityListener(@NonNull UIVisibilityListener listener);
    void removeVisibilityListener(@NonNull UIVisibilityListener listener);
}
