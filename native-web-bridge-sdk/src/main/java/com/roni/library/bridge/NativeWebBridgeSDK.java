package com.roni.library.bridge;

import android.content.Context;
import com.roni.library.contracts.common.RuntimeState;
import com.roni.library.logger.NativeLogger;

/**
 * Master Facade for NativeWebBridgeSDK integration.
 */
public final class NativeWebBridgeSDK {

    private static final String TAG = "NativeWebBridgeSDK";
    private static volatile NativeWebBridgeSDK sInstance;

    private Context context;
    private BridgeConfig config;
    private WebBridge bridge;
    private volatile RuntimeState state = RuntimeState.UNINITIALIZED;

    private NativeWebBridgeSDK() {
    }

    public static NativeWebBridgeSDK getInstance() {
        if (sInstance == null) {
            synchronized (NativeWebBridgeSDK.class) {
                if (sInstance == null) {
                    sInstance = new NativeWebBridgeSDK();
                }
            }
        }
        return sInstance;
    }

    public synchronized NativeWebBridgeSDKResult<WebBridge> init(Context context, BridgeConfig config) {
        this.context = context != null ? context.getApplicationContext() : null;
        this.config = config != null ? config : BridgeConfig.defaultConfig();
        try {
            state = RuntimeState.INITIALIZING;
            this.bridge = WebBridge.init(this.config);
            state = RuntimeState.READY;
            try {
                NativeLogger.getInstance().i(TAG, "NativeWebBridgeSDK initialized successfully. Version: 1.0.1");
            } catch (Throwable ignored) {
            }
            return NativeWebBridgeSDKResult.success(this.bridge);
        } catch (Throwable t) {
            state = RuntimeState.ERROR;
            try {
                NativeLogger.getInstance().e(TAG, "Failed to initialize NativeWebBridgeSDK: " + t.getMessage(), t);
            } catch (Throwable ignored) {
            }
            return NativeWebBridgeSDKResult.error(t.getMessage());
        }
    }

    public boolean isInitialized() {
        return state == RuntimeState.READY && bridge != null;
    }

    public RuntimeState getState() {
        return state;
    }

    public BridgeConfig getConfig() {
        return config;
    }

    public WebBridge getBridge() {
        return bridge;
    }

    public Context getContext() {
        return context;
    }

    public synchronized void release() {
        if (bridge != null) {
            bridge.release();
            bridge = null;
        }
        state = RuntimeState.TERMINATED;
        context = null;
        config = null;
        sInstance = null;
    }
}
