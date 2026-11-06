package com.roni.library.ui;

import com.roni.library.ui.navigation.NavigationManager;
import com.roni.library.ui.toolbar.ToolbarManager;
import org.junit.Assert;
import org.junit.Test;
import java.util.concurrent.atomic.AtomicBoolean;

public class NavigationAndToolbarTest {

    @Test
    public void testNavigationManager() {
        NavigationManager nav = new NavigationManager();
        Assert.assertEquals(0, nav.getTabCount());

        nav.addTab("home", "Home");
        nav.addTab("profile", "Profile");
        nav.addTab("settings", "Settings");

        Assert.assertEquals(3, nav.getTabCount());
        Assert.assertEquals("home", nav.getSelectedTab());

        nav.selectTab("settings");
        Assert.assertEquals("settings", nav.getSelectedTab());

        nav.setBadge("profile", 4);
        Assert.assertEquals(4, nav.getBadge("profile"));
        Assert.assertEquals(0, nav.getBadge("home"));

        nav.clear();
        Assert.assertEquals(0, nav.getTabCount());
    }

    @Test
    public void testToolbarManager() {
        ToolbarManager toolbar = new ToolbarManager();
        toolbar.setTitle("Dashboard");
        toolbar.setSubtitle("Live Telemetry");
        toolbar.setNavigationIconVisible(false);

        Assert.assertEquals("Dashboard", toolbar.getTitle());
        Assert.assertEquals("Live Telemetry", toolbar.getSubtitle());
        Assert.assertFalse(toolbar.isNavigationIconVisible());

        final AtomicBoolean executed = new AtomicBoolean(false);
        toolbar.addAction("search", "Search", new Runnable() {
            @Override
            public void run() {
                executed.set(true);
            }
        });

        Assert.assertEquals(1, toolbar.getActionCount());
        toolbar.getActions().get(0).execute();
        Assert.assertTrue(executed.get());

        toolbar.clearActions();
        Assert.assertEquals(0, toolbar.getActionCount());
    }
}
