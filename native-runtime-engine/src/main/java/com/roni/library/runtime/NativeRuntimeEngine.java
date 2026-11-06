package com.roni.library.runtime;

import android.content.Context;
import com.roni.library.runtime.handler.RuntimeCommandHandler;
import com.roni.library.runtime.service.NativeRuntimeService;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Thread-safe singleton entry point for NativeRuntimeEngine adhering to API.md.
 */
public class NativeRuntimeEngine {

    private static volatile NativeRuntimeEngine instance;

    private final AtomicBoolean initialized = new AtomicBoolean(false);
    private NativeRuntimeEngineConfig config;
    private NativeRuntimeService service;
    private RuntimeCommandHandler commandHandler;
    private WebViewConfigurator configurator;
    private WebResourceInterceptor interceptor;
    private CookieSyncManager cookieSyncManager;

    private NativeRuntimeEngine() {}

    public static NativeRuntimeEngine getInstance() {
        if (instance == null) {
            synchronized (NativeRuntimeEngine.class) {
                if (instance == null) {
                    instance = new NativeRuntimeEngine();
                }
            }
        }
        return instance;
    }

    public synchronized NativeRuntimeEngineResult<Boolean> init(Context context, NativeRuntimeEngineConfig config) {
        if (initialized.get()) {
            return NativeRuntimeEngineResult.success(true);
        }

        this.config = config != null ? config : NativeRuntimeEngineConfig.defaultConfiguration();
        this.service = new NativeRuntimeService();
        this.service.initialize();
        this.service.start();

        this.commandHandler = new RuntimeCommandHandler(this.service);
        this.configurator = new WebViewConfigurator(this.config);
        this.interceptor = new WebResourceInterceptor(this.service.getWebSourceResolver());
        this.cookieSyncManager = this.service.getCookieSyncManager();

        this.initialized.set(true);
        return NativeRuntimeEngineResult.success(true);
    }

    public boolean isInitialized() {
        return initialized.get();
    }

    public NativeRuntimeEngineConfig getConfig() {
        return config;
    }

    public NativeRuntimeService getService() {
        return service;
    }

    public RuntimeCommandHandler getCommandHandler() {
        return commandHandler;
    }

    public WebViewConfigurator getConfigurator() {
        checkInitialized();
        return configurator;
    }

    public WebResourceInterceptor getInterceptor() {
        checkInitialized();
        return interceptor;
    }

    public CookieSyncManager getCookieSyncManager() {
        checkInitialized();
        return cookieSyncManager;
    }

    public synchronized void release() {
        if (service != null) {
            service.stop();
            service.dispose();
            service = null;
        }
        commandHandler = null;
        configurator = null;
        interceptor = null;
        cookieSyncManager = null;
        config = null;
        initialized.set(false);
        instance = null;
    }

    private void checkInitialized() {
        if (!initialized.get()) {
            init(null, NativeRuntimeEngineConfig.defaultConfiguration());
        }
    }
}
