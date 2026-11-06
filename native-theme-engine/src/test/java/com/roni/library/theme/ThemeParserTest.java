package com.roni.library.theme;

import com.roni.library.theme.model.ThemeConfig;
import com.roni.library.theme.model.ThemeConfig.ThemeMode;
import com.roni.library.theme.parser.ThemeParser;
import org.junit.Assert;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class ThemeParserTest {

    @Test
    public void testParseEmptyMap() {
        ThemeConfig config = ThemeParser.parse(null);
        Assert.assertNotNull(config);
        Assert.assertEquals("DefaultLight", config.getThemeName());
        Assert.assertFalse(config.isDarkMode());

        ThemeConfig configEmpty = ThemeParser.parse(new HashMap<>());
        Assert.assertNotNull(configEmpty);
        Assert.assertEquals("DefaultLight", configEmpty.getThemeName());
    }

    @Test
    public void testParseCustomMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("themeName", "CyberNeon");
        map.put("darkMode", true);
        map.put("themeMode", "DARK");
        map.put("primaryColor", "#FF0055");
        map.put("secondaryColor", "#00FFAA");
        map.put("backgroundColor", "#0A0A10");
        map.put("surfaceColor", "#151522");
        map.put("textColor", "#FFFFFF");
        map.put("statusBarColor", "#0A0A10");
        map.put("navigationBarColor", "#0A0A10");
        map.put("statusBarIconsLight", false);
        map.put("navigationBarIconsLight", false);

        ThemeConfig config = ThemeParser.parse(map);
        Assert.assertEquals("CyberNeon", config.getThemeName());
        Assert.assertTrue(config.isDarkMode());
        Assert.assertEquals(ThemeMode.DARK, config.getThemeMode());
        Assert.assertEquals(0xFFFF0055, config.getPrimaryColorInt());
        Assert.assertEquals(0xFF00FFAA, config.getSecondaryColor());
        Assert.assertEquals(0xFF0A0A10, config.getBackgroundColor());
        Assert.assertFalse(config.isStatusBarIconsLight());
    }
}
