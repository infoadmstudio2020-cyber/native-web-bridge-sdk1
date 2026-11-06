package com.roni.library.platform.service;

import com.roni.library.platform.PlatformEngine;
import com.roni.library.service.BaseService;

/**
 * Layer 1 ecosystem integration service exposing NativePlatformEngine to ServiceRegistry.
 *
 * @author infoadmstudio2020-cyber
 */
public class NativePlatformEngineService extends BaseService {

    public static final String SERVICE_ID = "service.platform";
    public static final String SERVICE_NAME = "NativePlatformEngineService";

    private final PlatformEngine engine;

    public NativePlatformEngineService() {
        this(PlatformEngine.getInstance());
    }

    public NativePlatformEngineService(PlatformEngine engine) {
        super(SERVICE_ID, 200);
        this.engine = engine != null ? engine : PlatformEngine.getInstance();
    }

    @Override
    public String getServiceName() {
        return SERVICE_NAME;
    }

    @Override
    protected void onInitialize() {
        if (engine != null && !engine.isInitialized()) {
            engine.initialize();
        }
    }

    @Override
    protected void onServiceStart() {
        if (engine != null && !engine.isInitialized()) {
            engine.initialize();
        }
    }

    @Override
    protected void onServiceStop() {
        // Platform service pause
    }

    @Override
    protected void onDispose() {
        if (engine != null && !engine.isDisposed()) {
            engine.release();
        }
    }

    public PlatformEngine getEngine() {
        return engine;
    }
}
