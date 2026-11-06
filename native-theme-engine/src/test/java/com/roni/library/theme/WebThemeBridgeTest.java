package com.roni.library.theme;

import org.junit.Assert;
import org.junit.Test;

public class WebThemeBridgeTest {

    @Test
    public void testGenerateCssVariables() {
        ThemePalette light = ThemePalette.defaultLight();
        String css = WebThemeBridge.generateCssVariables(light);

        Assert.assertNotNull(css);
        Assert.assertTrue(css.contains(":root {"));
        Assert.assertTrue(css.contains("--color-primary: #6200EE;"));
        Assert.assertTrue(css.contains("--theme-dark-mode: false;"));
    }

    @Test
    public void testGenerateInjectionScript() {
        ThemePalette dark = ThemePalette.defaultDark();
        String script = WebThemeBridge.generateInjectionScript(dark);

        Assert.assertNotNull(script);
        Assert.assertTrue(script.contains("__native_theme_engine_styles__"));
        Assert.assertTrue(script.contains("nativeThemeChanged"));
        Assert.assertTrue(script.contains("darkMode: true"));
    }
}
