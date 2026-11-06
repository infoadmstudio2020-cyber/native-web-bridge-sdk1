package com.roni.library.ui.event;

import androidx.annotation.NonNull;
import com.roni.library.contracts.ui.IUIEventDispatcher;
import com.roni.library.contracts.ui.IUIEventListener;
import com.roni.library.contracts.ui.UIEvent;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class UIEventDispatcher implements IUIEventDispatcher {

    private final Map<String, List<IUIEventListener>> listeners = new ConcurrentHashMap<>();
    private final List<IUIEventListener> globalListeners = new CopyOnWriteArrayList<>();

    public UIEventDispatcher() {}

    @Override
    public void dispatchEvent(@NonNull UIEvent event) {
        List<IUIEventListener> typeListeners = listeners.get(event.getEventType());
        if (typeListeners != null) {
            for (IUIEventListener l : typeListeners) {
                try {
                    l.onUIEvent(event);
                } catch (Throwable ignored) {}
            }
        }
        for (IUIEventListener l : globalListeners) {
            try {
                l.onUIEvent(event);
            } catch (Throwable ignored) {}
        }
    }

    @Override
    public void addEventListener(@NonNull String eventType, @NonNull IUIEventListener listener) {
        if ("*".equals(eventType)) {
            globalListeners.add(listener);
        } else {
            listeners.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(listener);
        }
    }

    @Override
    public void removeEventListener(@NonNull String eventType, @NonNull IUIEventListener listener) {
        if ("*".equals(eventType)) {
            globalListeners.remove(listener);
        } else {
            List<IUIEventListener> list = listeners.get(eventType);
            if (list != null) {
                list.remove(listener);
            }
        }
    }

    @Override
    public void clearListeners() {
        listeners.clear();
        globalListeners.clear();
    }
}
