package com.roni.library.ui.controller;

import android.content.Context;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.roni.library.contracts.ui.IUIComponentController;
import java.util.Map;

public class UIComponentController implements IUIComponentController {

    protected Context context;
    protected View boundView;

    public UIComponentController() {}

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
    }

    @Override
    public void onInit(@Nullable Map<String, Object> config) {}

    @Override
    public void onRender(@NonNull View view) {
        this.boundView = view;
    }

    @Override
    public void onVisibilityChanged(boolean visible) {
        if (boundView != null) {
            boundView.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onStateChanged(@NonNull String stateKey, @Nullable Object value) {}

    @Override
    public void onDetach() {
        this.boundView = null;
        this.context = null;
    }

    @Override
    public void onDestroy() {
        onDetach();
    }
}
