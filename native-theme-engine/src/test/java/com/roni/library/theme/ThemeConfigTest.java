package com.roni.library.theme;

import com.roni.library.theme.model.ThemeConfig;
import com.roni.library.theme.model.ThemeConfig.ThemeMode;
import org.junit.Assert;
import org.junit.Test;

public class ThemeConfigTest {

    @Test
    public void testDefaultConfigurations() {
        ThemeConfig light = ThemeConfig.defaultLight();
        Assert.assertEquals("DefaultLight", light.getThemeName());
        Assert.assertEquals(ThemeMode.LIGHT, light.getThemeMode());
        Assert.assertFalse(light.isDarkMode());
        Assert.assertEquals("#6200EE", light.getPrimaryColor());
        Assert.assertEquals(0xFF6200EE, light.getPrimaryColorInt());
        Assert.assertEquals("#03DAC5", light.getSecondaryColorHex());
        Assert.assertEquals(0xFF03DAC5, light.getSecondaryColor());
        Assert.assertEquals("#FFFFFF", light.getBackgroundColorHex());
        Assert.assertEquals(0xFFFFFFFF, light.getBackgroundColor());

        ThemeConfig dark = ThemeConfig.defaultDark();
        Assert.assertEquals("DefaultDark", dark.getThemeName());
        Assert.assertEquals(ThemeMode.DARK, dark.getThemeMode());
        Assert.assertTrue(dark.isDarkMode());
        Assert.assertEquals("#BB86FC", dark.getPrimaryColor());
        Assert.assertEquals(0xFFBB86FC, dark.getPrimaryColorInt());
        Assert.assertEquals("#121212", dark.getBackgroundColorHex());
    }
}
