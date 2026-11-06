package com.roni.library.theme.observer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Tracks system-level theme state (light / dark) and notifies registered listeners.
 */
public class SystemThemeObserver {

    public interface SystemThemeListener {
        void onSystemThemeChanged(boolean isDark);
    }

    private final List<SystemThemeListener> listeners = new CopyOnWriteArrayList<>();
    private volatile boolean systemDark = false;

    public SystemThemeObserver() {}

    public SystemThemeObserver(boolean initialDark) {
        this.systemDark = initialDark;
    }

    public boolean isSystemDark() {
        return systemDark;
    }

    public synchronized void setSystemDark(boolean dark) {
        if (this.systemDark != dark) {
            this.systemDark = dark;
            notifyListeners(dark);
        }
    }

    public void addListener(SystemThemeListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(SystemThemeListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    private void notifyListeners(boolean isDark) {
        for (SystemThemeListener listener : listeners) {
            try {
                listener.onSystemThemeChanged(isDark);
            } catch (Throwable ignored) {}
        }
    }
}
