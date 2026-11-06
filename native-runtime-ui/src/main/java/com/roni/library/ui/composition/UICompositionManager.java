package com.roni.library.ui.composition;

import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.roni.library.contracts.ui.IUIComponent;
import com.roni.library.contracts.ui.IUICompositionManager;
import com.roni.library.contracts.ui.UICategory;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UICompositionManager implements IUICompositionManager {

    private final Map<UICategory, ViewGroup> categoryContainers = new ConcurrentHashMap<>();

    public UICompositionManager() {}

    @Override
    public void compose(@NonNull ViewGroup rootView, @NonNull List<IUIComponent> components) {
        // Sort components by priority descending
        components.sort(Comparator.comparingInt((IUIComponent c) -> c.getDescriptor().getPriority()).reversed());

        for (IUIComponent comp : components) {
            if (!comp.isEnabled()) continue;

            View view = comp.getView();
            if (view == null) {
                view = comp.render(rootView.getContext(), rootView);
            }

            if (view != null && view.getParent() == null) {
                ViewGroup target = categoryContainers.get(comp.getType().getCategory());
                if (target != null) {
                    target.addView(view);
                } else {
                    rootView.addView(view);
                }
            }
        }
    }

    @Nullable
    @Override
    public ViewGroup getContainerForCategory(@NonNull UICategory category) {
        return categoryContainers.get(category);
    }

    @Override
    public void registerCustomContainer(@NonNull UICategory category, @NonNull ViewGroup container) {
        categoryContainers.put(category, container);
    }

    public void clear() {
        categoryContainers.clear();
    }
}
