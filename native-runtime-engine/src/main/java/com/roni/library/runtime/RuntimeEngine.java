package com.roni.library.runtime;

import android.content.Context;

/**
 * Public static facade for NativeRuntimeEngine adhering to PROMPTFORNativeRuntimeEngine.txt.
 */
public final class RuntimeEngine {

    private RuntimeEngine() {}

    public static NativeRuntimeEngine getInstance() {
        return NativeRuntimeEngine.getInstance();
    }

    public static NativeRuntimeEngineResult<Boolean> init(Context context, NativeRuntimeEngineConfig config) {
        return NativeRuntimeEngine.getInstance().init(context, config);
    }

    public static boolean isInitialized() {
        return NativeRuntimeEngine.getInstance().isInitialized();
    }

    public static WebViewConfigurator getConfigurator() {
        return NativeRuntimeEngine.getInstance().getConfigurator();
    }

    public static WebResourceInterceptor getInterceptor() {
        return NativeRuntimeEngine.getInstance().getInterceptor();
    }

    public static CookieSyncManager getCookieSyncManager() {
        return NativeRuntimeEngine.getInstance().getCookieSyncManager();
    }

    public static void release() {
        NativeRuntimeEngine.getInstance().release();
    }
}
