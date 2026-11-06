package com.roni.library.contracts.ui;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface IUIStateManager {
    @NonNull
    UIState getComponentState(@NonNull UIComponentType type);
    void saveAllStates(@NonNull Bundle outState);
    void restoreAllStates(@NonNull Bundle inState);
    void reset();
}
