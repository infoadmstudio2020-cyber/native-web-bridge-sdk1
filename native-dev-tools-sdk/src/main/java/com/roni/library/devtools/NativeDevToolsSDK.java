package com.roni.library.devtools;

import android.content.Context;
import com.roni.library.devtools.handler.DevToolsCommandHandler;
import com.roni.library.devtools.service.NativeDevToolsService;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Thread-safe singleton entry point for NativeDevToolsSDK complying with API.md.
 */
public class NativeDevToolsSDK {

    private static volatile NativeDevToolsSDK instance;

    private final AtomicBoolean initialized = new AtomicBoolean(false);
    private NativeDevToolsSDKConfig config;
    private NativeDevToolsService service;
    private DevToolsCommandHandler commandHandler;
    private DebugDrawer debugDrawer;
    private NetworkInspector networkInspector;
    private FpsMonitor fpsMonitor;
    private CrashReporter crashReporter;

    private NativeDevToolsSDK() {}

    public static NativeDevToolsSDK getInstance() {
        if (instance == null) {
            synchronized (NativeDevToolsSDK.class) {
                if (instance == null) {
                    instance = new NativeDevToolsSDK();
                }
            }
        }
        return instance;
    }

    public synchronized NativeDevToolsSDKResult<Boolean> init(Context context, NativeDevToolsSDKConfig config) {
        if (initialized.get()) {
            return NativeDevToolsSDKResult.success(true);
        }

        this.config = config != null ? config : NativeDevToolsSDKConfig.defaultConfiguration();
        this.service = new NativeDevToolsService();
        this.service.initialize();
        this.service.start();

        this.commandHandler = new DevToolsCommandHandler(this.service);
        this.debugDrawer = new DebugDrawer();
        this.networkInspector = new NetworkInspector(this.service.getNetworkTrafficInspector());
        this.fpsMonitor = new FpsMonitor();
        this.fpsMonitor.start();

        this.crashReporter = new CrashReporter();
        this.crashReporter.install();

        this.initialized.set(true);
        return NativeDevToolsSDKResult.success(true);
    }

    public boolean isInitialized() {
        return initialized.get();
    }

    public NativeDevToolsSDKConfig getConfig() {
        return config;
    }

    public NativeDevToolsService getService() {
        return service;
    }

    public DevToolsCommandHandler getCommandHandler() {
        return commandHandler;
    }

    public DebugDrawer getDebugDrawer() {
        checkInitialized();
        return debugDrawer;
    }

    public NetworkInspector getNetworkInspector() {
        checkInitialized();
        return networkInspector;
    }

    public FpsMonitor getFpsMonitor() {
        checkInitialized();
        return fpsMonitor;
    }

    public CrashReporter getCrashReporter() {
        checkInitialized();
        return crashReporter;
    }

    public synchronized void release() {
        if (fpsMonitor != null) {
            fpsMonitor.stop();
            fpsMonitor = null;
        }
        if (service != null) {
            service.stop();
            service.dispose();
            service = null;
        }
        commandHandler = null;
        debugDrawer = null;
        networkInspector = null;
        crashReporter = null;
        config = null;
        initialized.set(false);
        instance = null;
    }

    private void checkInitialized() {
        if (!initialized.get()) {
            init(null, NativeDevToolsSDKConfig.defaultConfiguration());
        }
    }
}
