package com.roni.library.theme;

import android.content.Context;
import com.roni.library.contracts.theme.IThemeConfig;
import com.roni.library.theme.handler.ThemeCommandHandler;
import com.roni.library.theme.manager.ThemeManager;
import com.roni.library.theme.model.ThemeConfig;
import com.roni.library.theme.model.ThemeConfig.ThemeMode;
import com.roni.library.theme.service.NativeThemeService;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Thread-safe singleton entry point for NativeThemeEngine complying with API.md.
 */
public class NativeThemeEngine {

    private static volatile NativeThemeEngine instance;

    private final AtomicBoolean initialized = new AtomicBoolean(false);
    private NativeThemeEngineConfig config;
    private NativeThemeService themeService;
    private ThemeCommandHandler commandHandler;

    private NativeThemeEngine() {}

    public static NativeThemeEngine getInstance() {
        if (instance == null) {
            synchronized (NativeThemeEngine.class) {
                if (instance == null) {
                    instance = new NativeThemeEngine();
                }
            }
        }
        return instance;
    }

    public synchronized NativeThemeEngineResult<Boolean> init(Context context, NativeThemeEngineConfig config) {
        if (initialized.get()) {
            return NativeThemeEngineResult.success(true);
        }

        this.config = config != null ? config : NativeThemeEngineConfig.defaultConfiguration();
        this.themeService = new NativeThemeService(new ThemeManager());
        this.themeService.initialize();
        this.themeService.start();

        this.commandHandler = new ThemeCommandHandler(this.themeService.getThemeManager());

        this.initialized.set(true);
        return NativeThemeEngineResult.success(true);
    }

    public boolean isInitialized() {
        return initialized.get();
    }

    public NativeThemeEngineConfig getConfig() {
        return config;
    }

    public NativeThemeService getService() {
        return themeService;
    }

    public ThemeManager getThemeManager() {
        return themeService != null ? themeService.getThemeManager() : null;
    }

    public ThemeCommandHandler getCommandHandler() {
        return commandHandler;
    }

    public IThemeConfig getActiveTheme() {
        checkInitialized();
        return themeService.getActiveTheme();
    }

    public void applyTheme(IThemeConfig theme) {
        checkInitialized();
        themeService.applyTheme(theme);
    }

    public void setThemeMode(ThemeMode mode) {
        checkInitialized();
        themeService.setThemeMode(mode);
    }

    public void addObserver(ThemeObserver observer) {
        checkInitialized();
        if (observer != null && themeService != null) {
            themeService.getThemeManager().addListener(new ThemeManager.ThemeChangeListener() {
                @Override
                public void onThemeChanged(IThemeConfig newTheme) {
                    observer.onThemeChanged(newTheme);
                }
            });
        }
    }

    public synchronized void release() {
        if (themeService != null) {
            themeService.stop();
            themeService.dispose();
            themeService = null;
        }
        commandHandler = null;
        config = null;
        initialized.set(false);
        instance = null;
    }

    private void checkInitialized() {
        if (!initialized.get()) {
            init(null, NativeThemeEngineConfig.defaultConfiguration());
        }
    }
}
