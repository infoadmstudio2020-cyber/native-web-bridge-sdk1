package com.roni.library.theme;

import com.roni.library.contracts.theme.IThemeConfig;
import com.roni.library.theme.manager.ThemeManager;
import com.roni.library.theme.model.ThemeConfig;
import com.roni.library.theme.model.ThemeConfig.ThemeMode;
import org.junit.Assert;
import org.junit.Test;
import java.util.concurrent.atomic.AtomicReference;

public class ThemeManagerTest {

    @Test
    public void testModeSwitchingAndListeners() {
        ThemeManager manager = new ThemeManager();
        Assert.assertNotNull(manager.getActiveTheme());
        Assert.assertEquals(ThemeMode.SYSTEM, manager.getCurrentMode());

        final AtomicReference<IThemeConfig> changedTheme = new AtomicReference<>();
        manager.addListener(new ThemeManager.ThemeChangeListener() {
            @Override
            public void onThemeChanged(IThemeConfig newTheme) {
                changedTheme.set(newTheme);
            }
        });

        // Set DARK mode
        manager.setThemeMode(ThemeMode.DARK);
        Assert.assertEquals(ThemeMode.DARK, manager.getCurrentMode());
        Assert.assertTrue(manager.getActiveTheme().isDarkMode());
        Assert.assertNotNull(changedTheme.get());
        Assert.assertTrue(changedTheme.get().isDarkMode());

        // Set LIGHT mode
        manager.setThemeMode(ThemeMode.LIGHT);
        Assert.assertEquals(ThemeMode.LIGHT, manager.getCurrentMode());
        Assert.assertFalse(manager.getActiveTheme().isDarkMode());

        // Custom apply theme
        ThemeConfig custom = new ThemeConfig("Custom", ThemeMode.DARK, true, 0, 0, 0, 0, 0, 0, 0, false, false);
        manager.applyTheme(custom);
        Assert.assertEquals("Custom", manager.getActiveTheme().getThemeName());
    }

    @Test
    public void testSystemObserverIntegration() {
        ThemeManager manager = new ThemeManager();
        manager.setThemeMode(ThemeMode.SYSTEM);

        // System changes to dark
        manager.getSystemThemeObserver().setSystemDark(true);
        Assert.assertTrue(manager.getActiveTheme().isDarkMode());

        // System changes to light
        manager.getSystemThemeObserver().setSystemDark(false);
        Assert.assertFalse(manager.getActiveTheme().isDarkMode());
    }
}
