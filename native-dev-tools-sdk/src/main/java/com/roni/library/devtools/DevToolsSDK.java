package com.roni.library.devtools;

import android.content.Context;

/**
 * Public static facade for NativeDevToolsSDK complying with PROMPTFORNativeDevToolsSDK.txt.
 */
public final class DevToolsSDK {

    private DevToolsSDK() {}

    public static NativeDevToolsSDK getInstance() {
        return NativeDevToolsSDK.getInstance();
    }

    public static NativeDevToolsSDKResult<Boolean> init(Context context, NativeDevToolsSDKConfig config) {
        return NativeDevToolsSDK.getInstance().init(context, config);
    }

    public static boolean isInitialized() {
        return NativeDevToolsSDK.getInstance().isInitialized();
    }

    public static DebugDrawer getDebugDrawer() {
        return NativeDevToolsSDK.getInstance().getDebugDrawer();
    }

    public static NetworkInspector getNetworkInspector() {
        return NativeDevToolsSDK.getInstance().getNetworkInspector();
    }

    public static FpsMonitor getFpsMonitor() {
        return NativeDevToolsSDK.getInstance().getFpsMonitor();
    }

    public static CrashReporter getCrashReporter() {
        return NativeDevToolsSDK.getInstance().getCrashReporter();
    }

    public static void release() {
        NativeDevToolsSDK.getInstance().release();
    }
}
