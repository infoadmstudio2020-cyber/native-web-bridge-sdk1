package com.roni.library.theme.manager;

import com.roni.library.contracts.theme.IThemeConfig;
import com.roni.library.theme.model.ThemeConfig;
import com.roni.library.theme.model.ThemeConfig.ThemeMode;
import com.roni.library.theme.observer.SystemThemeObserver;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Coordinates theme application, mode switching (SYSTEM / LIGHT / DARK),
 * and observer dispatch.
 */
public class ThemeManager {

    public interface ThemeChangeListener {
        void onThemeChanged(IThemeConfig newTheme);
    }

    private final SystemThemeObserver systemThemeObserver;
    private final List<ThemeChangeListener> listeners = new CopyOnWriteArrayList<>();
    private volatile IThemeConfig activeTheme;
    private volatile ThemeMode currentMode = ThemeMode.SYSTEM;

    public ThemeManager() {
        this.systemThemeObserver = new SystemThemeObserver();
        this.activeTheme = ThemeConfig.defaultLight();

        this.systemThemeObserver.addListener(new SystemThemeObserver.SystemThemeListener() {
            @Override
            public void onSystemThemeChanged(boolean isDark) {
                if (currentMode == ThemeMode.SYSTEM) {
                    applyTheme(isDark ? ThemeConfig.defaultDark() : ThemeConfig.defaultLight());
                }
            }
        });
    }

    public synchronized void applyTheme(IThemeConfig theme) {
        if (theme == null) {
            return;
        }
        this.activeTheme = theme;
        notifyThemeChanged(theme);
    }

    public synchronized void setThemeMode(ThemeMode mode) {
        if (mode == null) {
            mode = ThemeMode.SYSTEM;
        }
        this.currentMode = mode;
        switch (mode) {
            case LIGHT:
                applyTheme(ThemeConfig.defaultLight());
                break;
            case DARK:
                applyTheme(ThemeConfig.defaultDark());
                break;
            case SYSTEM:
                applyTheme(systemThemeObserver.isSystemDark() ? ThemeConfig.defaultDark() : ThemeConfig.defaultLight());
                break;
        }
    }

    public IThemeConfig getActiveTheme() {
        return activeTheme;
    }

    public ThemeMode getCurrentMode() {
        return currentMode;
    }

    public SystemThemeObserver getSystemThemeObserver() {
        return systemThemeObserver;
    }

    public void addListener(ThemeChangeListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(ThemeChangeListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    private void notifyThemeChanged(IThemeConfig newTheme) {
        for (ThemeChangeListener listener : listeners) {
            try {
                listener.onThemeChanged(newTheme);
            } catch (Throwable ignored) {}
        }
    }
}
