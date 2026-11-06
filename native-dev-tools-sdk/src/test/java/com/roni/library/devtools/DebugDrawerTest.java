package com.roni.library.devtools;

import org.junit.Assert;
import org.junit.Test;
import java.util.concurrent.atomic.AtomicBoolean;

public class DebugDrawerTest {

    @Test
    public void testDrawerVisibilityAndPanels() {
        DebugDrawer drawer = new DebugDrawer();
        Assert.assertFalse(drawer.isVisible());
        Assert.assertTrue(drawer.isBubbleEnabled());

        final AtomicBoolean notified = new AtomicBoolean(false);
        DebugDrawer.DrawerVisibilityListener listener = new DebugDrawer.DrawerVisibilityListener() {
            @Override
            public void onVisibilityChanged(boolean visible) {
                notified.set(visible);
            }
        };

        drawer.addVisibilityListener(listener);
        drawer.show();
        Assert.assertTrue(drawer.isVisible());
        Assert.assertTrue(notified.get());

        drawer.toggle();
        Assert.assertFalse(drawer.isVisible());
        Assert.assertFalse(notified.get());

        drawer.addPanel("CustomInspectors");
        Assert.assertTrue(drawer.getActivePanels().contains("CustomInspectors"));

        drawer.removeVisibilityListener(listener);
    }
}
