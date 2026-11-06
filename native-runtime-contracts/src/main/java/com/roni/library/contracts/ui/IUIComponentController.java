package com.roni.library.contracts.ui;

import android.content.Context;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.Map;

public interface IUIComponentController {
    void onAttach(@NonNull Context context);
    void onInit(@Nullable Map<String, Object> config);
    void onRender(@NonNull View view);
    void onVisibilityChanged(boolean visible);
    void onStateChanged(@NonNull String stateKey, @Nullable Object value);
    void onDetach();
    void onDestroy();
}
