package com.roni.library.runtime.service;

import com.roni.library.contracts.common.RuntimeState;
import com.roni.library.service.BaseService;
import com.roni.library.service.IService;
import com.roni.library.service.ServiceRegistry;
import com.roni.library.runtime.CookieSyncManager;
import com.roni.library.runtime.container.WebViewContainer;
import com.roni.library.runtime.server.LocalWebServer;
import com.roni.library.runtime.source.WebSourceResolver;
import com.roni.library.runtime.vitals.WebVitalsCollector;

/**
 * Layer 2 Domain Service for NativeRuntimeEngine extending BaseService.
 */
public class NativeRuntimeService extends BaseService implements IService {

    private final LocalWebServer localWebServer;
    private final WebSourceResolver webSourceResolver;
    private final WebViewContainer defaultContainer;
    private final CookieSyncManager cookieSyncManager;

    public NativeRuntimeService() {
        this(new WebSourceResolver());
    }

    public NativeRuntimeService(WebSourceResolver resolver) {
        super("NativeRuntimeService", 90);
        this.webSourceResolver = resolver != null ? resolver : new WebSourceResolver();
        this.localWebServer = new LocalWebServer(8080, this.webSourceResolver);
        this.defaultContainer = new WebViewContainer("default-runtime-container");
        this.cookieSyncManager = new CookieSyncManager();
    }

    @Override
    protected void onInitialize() throws Exception {
        try {
            ServiceRegistry registry = ServiceRegistry.getInstance();
            if (registry != null) {
                registry.registerService(IService.class, this);
            }
        } catch (Throwable ignored) {}
    }

    @Override
    protected void onServiceStart() throws Exception {
        localWebServer.start();
    }

    @Override
    protected void onServiceStop() throws Exception {
        localWebServer.stop();
        defaultContainer.destroy();
    }

    @Override
    protected void onDispose() throws Exception {
        localWebServer.stop();
        webSourceResolver.clear();
        cookieSyncManager.clearAllCookies();
    }

    public LocalWebServer getLocalWebServer() {
        return localWebServer;
    }

    public WebSourceResolver getWebSourceResolver() {
        return webSourceResolver;
    }

    public WebViewContainer getDefaultContainer() {
        return defaultContainer;
    }

    public CookieSyncManager getCookieSyncManager() {
        return cookieSyncManager;
    }

    public WebVitalsCollector getVitalsCollector() {
        return defaultContainer.getVitalsCollector();
    }

    public RuntimeState getServiceState() {
        return RuntimeState.READY;
    }
}
