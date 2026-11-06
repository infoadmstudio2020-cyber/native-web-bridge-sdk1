package com.roni.library.ui;

import android.content.Context;

/**
 * Public facade conforming to PROMPTFORNativeRuntimeUI.txt specifications.
 */
public final class NativeUIHost {

    private NativeUIHost() {}

    public static NativeRuntimeUI getInstance() {
        return NativeRuntimeUI.getInstance();
    }

    public static NativeRuntimeUIResult<Boolean> init(Context context, NativeRuntimeUIConfig config) {
        return NativeRuntimeUI.getInstance().init(context, config);
    }

    public static boolean isInitialized() {
        return NativeRuntimeUI.getInstance().isInitialized();
    }

    public static NativeToast getToast() {
        return NativeRuntimeUI.getInstance().getToast();
    }

    public static LoadingIndicator getLoadingIndicator() {
        return NativeRuntimeUI.getInstance().getLoadingIndicator();
    }

    public static CustomBottomSheet getBottomSheet() {
        return NativeRuntimeUI.getInstance().getBottomSheet();
    }

    public static NativeActionSheet getActionSheet() {
        return NativeRuntimeUI.getInstance().getActionSheet();
    }

    public static void release() {
        NativeRuntimeUI.getInstance().release();
    }
}
