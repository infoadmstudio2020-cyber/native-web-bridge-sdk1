package com.roni.library.theme;

import com.roni.library.theme.observer.SystemThemeObserver;
import org.junit.Assert;
import org.junit.Test;
import java.util.concurrent.atomic.AtomicBoolean;

public class SystemThemeObserverTest {

    @Test
    public void testSystemThemeNotification() {
        SystemThemeObserver observer = new SystemThemeObserver(false);
        Assert.assertFalse(observer.isSystemDark());

        final AtomicBoolean receivedDark = new AtomicBoolean(false);
        SystemThemeObserver.SystemThemeListener listener = new SystemThemeObserver.SystemThemeListener() {
            @Override
            public void onSystemThemeChanged(boolean isDark) {
                receivedDark.set(isDark);
            }
        };

        observer.addListener(listener);
        observer.setSystemDark(true);

        Assert.assertTrue(observer.isSystemDark());
        Assert.assertTrue(receivedDark.get());

        // Test remove
        observer.removeListener(listener);
        receivedDark.set(false);
        observer.setSystemDark(false);
        Assert.assertFalse(receivedDark.get());
    }
}
