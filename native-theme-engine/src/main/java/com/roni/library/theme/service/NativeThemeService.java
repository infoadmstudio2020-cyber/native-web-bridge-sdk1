package com.roni.library.theme.service;

import com.roni.library.contracts.common.RuntimeState;
import com.roni.library.contracts.theme.IThemeConfig;
import com.roni.library.service.BaseService;
import com.roni.library.service.ServiceRegistry;
import com.roni.library.theme.manager.ThemeManager;
import com.roni.library.theme.model.ThemeConfig.ThemeMode;

/**
 * Layer 2 Domain Service for NativeThemeEngine extending BaseService.
 */
public class NativeThemeService extends BaseService implements com.roni.library.service.IService {

    private final ThemeManager themeManager;

    public NativeThemeService() {
        this(new ThemeManager());
    }

    public NativeThemeService(ThemeManager themeManager) {
        super("NativeThemeService", 100);
        this.themeManager = themeManager != null ? themeManager : new ThemeManager();
    }

    @Override
    protected void onInitialize() throws Exception {
        try {
            ServiceRegistry registry = ServiceRegistry.getInstance();
            if (registry != null) {
                registry.registerService(com.roni.library.service.IService.class, this);
            }
        } catch (Throwable ignored) {}
    }

    @Override
    protected void onServiceStart() throws Exception {
        // Ready for theme tracking
    }

    @Override
    protected void onServiceStop() throws Exception {
        // Stopped
    }

    @Override
    protected void onDispose() throws Exception {
        // Released
    }

    public ThemeManager getThemeManager() {
        return themeManager;
    }

    public IThemeConfig getActiveTheme() {
        return themeManager.getActiveTheme();
    }

    public void applyTheme(IThemeConfig theme) {
        themeManager.applyTheme(theme);
    }

    public void setThemeMode(ThemeMode mode) {
        themeManager.setThemeMode(mode);
    }

    public RuntimeState getServiceState() {
        return RuntimeState.READY;
    }
}

