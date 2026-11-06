package com.roni.library.ui.visibility;

import androidx.annotation.NonNull;
import com.roni.library.contracts.ui.IUIComponent;
import com.roni.library.contracts.ui.IUIComponentRegistry;
import com.roni.library.contracts.ui.IUIVisibilityManager;
import com.roni.library.contracts.ui.UIComponentType;
import com.roni.library.contracts.ui.UIVisibilityListener;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class UIVisibilityManager implements IUIVisibilityManager {

    private final IUIComponentRegistry registry;
    private final Map<UIComponentType, Boolean> visibilityStates = new ConcurrentHashMap<>();
    private final List<UIVisibilityListener> listeners = new CopyOnWriteArrayList<>();

    public UIVisibilityManager(@NonNull IUIComponentRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void setVisible(@NonNull UIComponentType type, boolean visible) {
        visibilityStates.put(type, visible);
        IUIComponent comp = registry.getComponent(type);
        if (comp != null) {
            comp.setVisible(visible);
        }
        for (UIVisibilityListener l : listeners) {
            try {
                l.onVisibilityChanged(type, visible);
            } catch (Throwable ignored) {}
        }
    }

    @Override
    public boolean isVisible(@NonNull UIComponentType type) {
        Boolean state = visibilityStates.get(type);
        if (state != null) {
            return state;
        }
        IUIComponent comp = registry.getComponent(type);
        return comp != null && comp.isVisible();
    }

    @Override
    public void toggle(@NonNull UIComponentType type) {
        setVisible(type, !isVisible(type));
    }

    @Override
    public void addVisibilityListener(@NonNull UIVisibilityListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeVisibilityListener(@NonNull UIVisibilityListener listener) {
        listeners.remove(listener);
    }

    public void clear() {
        visibilityStates.clear();
        listeners.clear();
    }
}
