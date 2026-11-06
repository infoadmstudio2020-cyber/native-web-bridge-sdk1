package com.roni.library.theme;

import org.junit.Assert;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class ThemePaletteTest {

    @Test
    public void testDefaultPalettes() {
        ThemePalette light = ThemePalette.defaultLight();
        Assert.assertEquals("DefaultLight", light.getThemeName());
        Assert.assertFalse(light.isDarkMode());
        Assert.assertEquals("#6200EE", light.getPrimaryColor());
        Assert.assertEquals("#FFFFFF", light.getBackgroundColor());
        Assert.assertTrue(light.isStatusBarIconsLight());

        ThemePalette dark = ThemePalette.defaultDark();
        Assert.assertEquals("DefaultDark", dark.getThemeName());
        Assert.assertTrue(dark.isDarkMode());
        Assert.assertEquals("#BB86FC", dark.getPrimaryColor());
        Assert.assertEquals("#121212", dark.getBackgroundColor());
        Assert.assertFalse(dark.isStatusBarIconsLight());
    }

    @Test
    public void testCustomTokens() {
        Map<String, String> tokens = new HashMap<>();
        tokens.put("brand-gradient", "linear-gradient(45deg, #FF0, #00F)");

        ThemePalette custom = new ThemePalette(
                "BrandTheme",
                false,
                "#112233",
                "#445566",
                "#FFFFFF",
                "#EEEEEE",
                "#000000",
                "#333333",
                "#FF0088",
                "#112233",
                "#FFFFFF",
                true,
                false,
                tokens
        );

        Assert.assertEquals("BrandTheme", custom.getThemeName());
        Assert.assertEquals("linear-gradient(45deg, #FF0, #00F)", custom.getToken("brand-gradient", "fallback"));
        Assert.assertEquals("fallback", custom.getToken("non-existent", "fallback"));
    }
}
