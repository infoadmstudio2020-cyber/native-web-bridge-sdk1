package com.roni.library.platform;

import android.content.Context;

import com.roni.library.contracts.common.IDisposable;
import com.roni.library.contracts.common.RuntimeState;
import com.roni.library.logger.NativeLogger;
import com.roni.library.platform.handler.PlatformCommandHandler;
import com.roni.library.platform.service.BatteryPlatformService;
import com.roni.library.platform.service.ClipboardPlatformService;
import com.roni.library.platform.service.DevicePlatformService;
import com.roni.library.platform.service.NativePlatformEngineService;
import com.roni.library.platform.service.NetworkPlatformService;
import com.roni.library.platform.service.StoragePlatformService;
import com.roni.library.platform.service.ToastNotificationService;
import com.roni.library.service.ServiceRegistry;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Primary public entry point and coordinator for NativePlatformEngine (Layer 2).
 * Orchestrates device specifications, battery, connectivity, storage, clipboard,
 * and haptic toast services with thread-safe lifecycle control.
 *
 * @author infoadmstudio2020-cyber
 */
public class PlatformEngine implements IDisposable {

    private static final String TAG = "PlatformEngine";
    private static volatile PlatformEngine sInstance;
    private static final Object INSTANCE_LOCK = new Object();

    private final AtomicReference<RuntimeState> runtimeState = new AtomicReference<>(RuntimeState.UNINITIALIZED);
    private final AtomicBoolean disposed = new AtomicBoolean(false);

    private final DevicePlatformService deviceService = new DevicePlatformService();
    private final BatteryPlatformService batteryService = new BatteryPlatformService();
    private final NetworkPlatformService networkService = new NetworkPlatformService();
    private final StoragePlatformService storageService = new StoragePlatformService();
    private final ClipboardPlatformService clipboardService = new ClipboardPlatformService();
    private final ToastNotificationService toastService = new ToastNotificationService();
    private final PlatformCommandHandler commandHandler = new PlatformCommandHandler();

    private volatile PlatformEngineConfig config;
    private volatile Context appContext;

    public PlatformEngine() {
        initialize();
    }

    /**
     * Retrieves the singleton instance of PlatformEngine.
     */
    public static PlatformEngine getInstance() {
        if (sInstance == null) {
            synchronized (INSTANCE_LOCK) {
                if (sInstance == null) {
                    sInstance = new PlatformEngine();
                }
            }
        }
        return sInstance;
    }

    /**
     * Sets or overrides the singleton instance (e.g. for testing).
     */
    public static void setInstance(PlatformEngine engine) {
        synchronized (INSTANCE_LOCK) {
            sInstance = engine;
        }
    }

    /**
     * Initializes the engine with default configuration.
     */
    public synchronized void initialize() {
        if (runtimeState.get() == RuntimeState.READY) {
            return;
        }
        init(null, new PlatformEngineConfig.Builder().build());
    }

    /**
     * Initializes the platform engine with Android context and custom configuration.
     *
     * @param context Android Application Context
     * @param config  Configuration options
     */
    public synchronized void init(Context context, PlatformEngineConfig config) {
        runtimeState.set(RuntimeState.INITIALIZING);
        disposed.set(false);

        if (context != null) {
            this.appContext = context.getApplicationContext();
        }
        this.config = config != null ? config : new PlatformEngineConfig.Builder().build();

        // Initialize sub-services with context
        deviceService.init(this.appContext);
        batteryService.init(this.appContext);
        networkService.init(this.appContext);
        storageService.init(this.appContext);
        clipboardService.init(this.appContext);
        toastService.init(this.appContext);

        // Register with Layer 1 ServiceRegistry
        try {
            NativePlatformEngineService service = new NativePlatformEngineService(this);
            ServiceRegistry.getInstance().registerService(
                    com.roni.library.contracts.service.IService.class,
                    service
            );
        } catch (Throwable ignored) {
        }

        runtimeState.set(RuntimeState.READY);

        try {
            NativeLogger.getInstance().i(TAG, "PlatformEngine initialized and ready.");
        } catch (Throwable ignored) {
        }
    }

    public boolean isInitialized() {
        return runtimeState.get() == RuntimeState.READY;
    }

    public RuntimeState getState() {
        return runtimeState.get();
    }

    public PlatformEngineConfig getConfig() {
        return config;
    }

    public Context getAppContext() {
        return appContext;
    }

    public DevicePlatformService getDeviceService() {
        return deviceService;
    }

    public BatteryPlatformService getBatteryService() {
        return batteryService;
    }

    public NetworkPlatformService getNetworkService() {
        return networkService;
    }

    public StoragePlatformService getStorageService() {
        return storageService;
    }

    public ClipboardPlatformService getClipboardService() {
        return clipboardService;
    }

    public ToastNotificationService getToastService() {
        return toastService;
    }

    public PlatformCommandHandler getCommandHandler() {
        return commandHandler;
    }

    /**
     * Releases all held platform resources and resets state.
     */
    public synchronized void release() {
        dispose();
    }

    @Override
    public synchronized void dispose() {
        if (disposed.compareAndSet(false, true)) {
            runtimeState.set(RuntimeState.TERMINATED);

            deviceService.release();
            batteryService.release();
            networkService.release();
            storageService.release();
            clipboardService.release();
            toastService.release();

            appContext = null;
            config = null;

            synchronized (INSTANCE_LOCK) {
                if (sInstance == this) {
                    sInstance = null;
                }
            }
        }
    }

    @Override
    public boolean isDisposed() {
        return disposed.get();
    }
}
