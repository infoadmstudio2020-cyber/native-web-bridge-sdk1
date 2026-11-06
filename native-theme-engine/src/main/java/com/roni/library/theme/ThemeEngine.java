package com.roni.library.theme;

import android.content.Context;
import com.roni.library.contracts.theme.IThemeConfig;
import com.roni.library.theme.model.ThemeConfig.ThemeMode;

/**
 * Public static facade for NativeThemeEngine complying with PROMPTFORNativeThemeEngine.txt.
 */
public final class ThemeEngine {

    private ThemeEngine() {}

    public static NativeThemeEngine getInstance() {
        return NativeThemeEngine.getInstance();
    }

    public static NativeThemeEngineResult<Boolean> init(Context context, NativeThemeEngineConfig config) {
        return NativeThemeEngine.getInstance().init(context, config);
    }

    public static boolean isInitialized() {
        return NativeThemeEngine.getInstance().isInitialized();
    }

    public static IThemeConfig getActiveTheme() {
        return NativeThemeEngine.getInstance().getActiveTheme();
    }

    public static void applyTheme(IThemeConfig theme) {
        NativeThemeEngine.getInstance().applyTheme(theme);
    }

    public static void setThemeMode(ThemeMode mode) {
        NativeThemeEngine.getInstance().setThemeMode(mode);
    }

    public static void addObserver(ThemeObserver observer) {
        NativeThemeEngine.getInstance().addObserver(observer);
    }

    public static void release() {
        NativeThemeEngine.getInstance().release();
    }
}
