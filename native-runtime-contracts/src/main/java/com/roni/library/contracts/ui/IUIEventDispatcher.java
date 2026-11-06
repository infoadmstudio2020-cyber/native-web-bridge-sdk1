package com.roni.library.contracts.ui;

import androidx.annotation.NonNull;

public interface IUIEventDispatcher {
    void dispatchEvent(@NonNull UIEvent event);
    void addEventListener(@NonNull String eventType, @NonNull IUIEventListener listener);
    void removeEventListener(@NonNull String eventType, @NonNull IUIEventListener listener);
    void clearListeners();
}
