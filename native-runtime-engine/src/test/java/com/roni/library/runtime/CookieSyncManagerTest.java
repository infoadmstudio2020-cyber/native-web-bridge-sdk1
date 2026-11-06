package com.roni.library.runtime;

import org.junit.Assert;
import org.junit.Test;
import java.util.List;

public class CookieSyncManagerTest {

    @Test
    public void testCookieManagement() {
        CookieSyncManager manager = new CookieSyncManager();

        manager.setCookie("example.com", "sessionId=abc123xyz");
        manager.setCookie("example.com", "theme=dark");
        manager.setCookie("sub.example.com", "region=eu");

        List<String> cookies = manager.getCookies("example.com");
        Assert.assertEquals(2, cookies.size());
        Assert.assertTrue(cookies.contains("sessionId=abc123xyz"));
        Assert.assertTrue(cookies.contains("theme=dark"));

        Assert.assertEquals(1, manager.getCookies("sub.example.com").size());
        Assert.assertEquals(0, manager.getCookies("unknown.com").size());

        manager.clearCookies("sub.example.com");
        Assert.assertEquals(0, manager.getCookies("sub.example.com").size());

        manager.clearAllCookies();
        Assert.assertEquals(0, manager.getCookies("example.com").size());
    }
}
