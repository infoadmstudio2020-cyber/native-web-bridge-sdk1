package com.roni.library.devtools;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Controller for in-app floating debug bubble and expandable debug drawer.
 */
public class DebugDrawer {

    public interface DrawerVisibilityListener {
        void onVisibilityChanged(boolean visible);
    }

    private volatile boolean visible = false;
    private volatile boolean bubbleEnabled = true;
    private final List<DrawerVisibilityListener> listeners = new CopyOnWriteArrayList<>();
    private final List<String> activePanels = new CopyOnWriteArrayList<>();

    public DebugDrawer() {
        activePanels.add("Network");
        activePanels.add("Performance");
        activePanels.add("CrashLogs");
        activePanels.add("Diagnostics");
    }

    public boolean isVisible() {
        return visible;
    }

    public void show() {
        if (!visible) {
            visible = true;
            notifyListeners(true);
        }
    }

    public void hide() {
        if (visible) {
            visible = false;
            notifyListeners(false);
        }
    }

    public void toggle() {
        if (visible) {
            hide();
        } else {
            show();
        }
    }

    public boolean isBubbleEnabled() {
        return bubbleEnabled;
    }

    public void setBubbleEnabled(boolean enabled) {
        this.bubbleEnabled = enabled;
    }

    public List<String> getActivePanels() {
        return new ArrayList<>(activePanels);
    }

    public void addPanel(String panelName) {
        if (panelName != null && !activePanels.contains(panelName)) {
            activePanels.add(panelName);
        }
    }

    public void addVisibilityListener(DrawerVisibilityListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeVisibilityListener(DrawerVisibilityListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    private void notifyListeners(boolean isVisible) {
        for (DrawerVisibilityListener l : listeners) {
            try {
                l.onVisibilityChanged(isVisible);
            } catch (Throwable ignored) {}
        }
    }
}
