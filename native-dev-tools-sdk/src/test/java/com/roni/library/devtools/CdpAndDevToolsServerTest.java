package com.roni.library.devtools;

import com.roni.library.devtools.protocol.CdpDomainDispatcher;
import com.roni.library.devtools.server.DevToolsServer;
import com.roni.library.devtools.server.DevToolsTarget;
import org.junit.Assert;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class CdpAndDevToolsServerTest {

    @Test
    public void testCdpDomainDispatch() {
        CdpDomainDispatcher dispatcher = new CdpDomainDispatcher();

        // Built-in Runtime.enable
        Map<String, Object> res = dispatcher.dispatch(1, "Runtime.enable", new HashMap<>());
        Assert.assertEquals(1, res.get("id"));
        Assert.assertNotNull(res.get("result"));

        // Unknown method
        Map<String, Object> err = dispatcher.dispatch(2, "InvalidMethod", new HashMap<>());
        Assert.assertTrue(err.containsKey("error"));

        // Custom domain
        dispatcher.registerDomain("CustomDomain", new CdpDomainDispatcher.DomainHandler() {
            @Override
            public Map<String, Object> handle(String method, Map<String, Object> params) {
                Map<String, Object> r = new HashMap<>();
                r.put("echoMethod", method);
                return r;
            }
        });

        Map<String, Object> customRes = dispatcher.dispatch(3, "CustomDomain.ping", new HashMap<>());
        Assert.assertNotNull(customRes.get("result"));
        Map<String, Object> r = (Map<String, Object>) customRes.get("result");
        Assert.assertEquals("ping", r.get("echoMethod"));
    }

    @Test
    public void testDevToolsServerTargets() {
        DevToolsServer server = new DevToolsServer("127.0.0.1", 9222);
        Assert.assertFalse(server.isRunning());

        server.start();
        Assert.assertTrue(server.isRunning());

        DevToolsTarget target = new DevToolsTarget("t-1", "Main WebView", "page", "Primary Activity", "https://app.local", null);
        server.registerTarget(target);

        Assert.assertTrue(server.activateTarget("t-1"));
        Assert.assertEquals(1, server.getListJson().size());

        Map<String, Object> version = server.getVersionJson();
        Assert.assertEquals("1.3", version.get("Protocol-Version"));

        server.unregisterTarget("t-1");
        Assert.assertEquals(0, server.getListJson().size());

        server.stop();
        Assert.assertFalse(server.isRunning());
    }
}
