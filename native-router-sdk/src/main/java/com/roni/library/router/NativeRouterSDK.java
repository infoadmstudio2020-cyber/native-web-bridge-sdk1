package com.roni.library.router;

import android.content.Context;

import com.roni.library.logger.NativeLogger;
import com.roni.library.router.service.NativeRouterService;
import com.roni.library.service.ServiceRegistry;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Main public entry point for the NativeRouterSDK library.
 *
 * @author infoadmstudio2020-cyber
 */
public class NativeRouterSDK {

    private static final String TAG = "NativeRouterSDK";
    private static volatile NativeRouterSDK sInstance;
    private static final Object LOCK = new Object();

    private final AtomicBoolean initialized = new AtomicBoolean(false);
    private volatile NativeRouterSDKConfig config;
    private volatile Context appContext;

    private NativeRouterSDK() {
    }

    /**
     * Returns the singleton instance of NativeRouterSDK.
     */
    public static NativeRouterSDK getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = new NativeRouterSDK();
                }
            }
        }
        return sInstance;
    }

    /**
     * Initializes the library with context and configuration.
     *
     * @param context Application context
     * @param config  Configuration object
     */
    public void init(Context context, NativeRouterSDKConfig config) {
        if (context != null) {
            this.appContext = context.getApplicationContext();
        }
        this.config = config != null ? config : new NativeRouterSDKConfig.Builder().build();

        // Initialize core NativeRouter engine
        NativeRouter router = NativeRouter.getInstance();
        router.initialize();

        // Register router service with NativeServiceSDK ServiceRegistry
        try {
            NativeRouterService service = new NativeRouterService(router);
            ServiceRegistry.getInstance().registerService(
                    com.roni.library.contracts.service.IService.class,
                    service
            );
        } catch (Throwable t) {
            // Service registry integration fallback
        }

        initialized.set(true);

        try {
            NativeLogger.getInstance().i(TAG, "NativeRouterSDK initialized successfully.");
        } catch (Throwable ignored) {
        }
    }

    /**
     * Checks if the SDK has been initialized.
     */
    public boolean isInitialized() {
        return initialized.get();
    }

    /**
     * Returns the current SDK configuration.
     */
    public NativeRouterSDKConfig getConfig() {
        return config;
    }

    /**
     * Returns the application context.
     */
    public Context getAppContext() {
        return appContext;
    }

    /**
     * Convenience access to the core router engine.
     */
    public NativeRouter getRouter() {
        return NativeRouter.getInstance();
    }

    /**
     * Releases all resources and resets the singleton instance.
     */
    public void release() {
        NativeRouter.getInstance().release();
        initialized.set(false);
        config = null;
        appContext = null;

        synchronized (LOCK) {
            sInstance = null;
        }
    }
}
