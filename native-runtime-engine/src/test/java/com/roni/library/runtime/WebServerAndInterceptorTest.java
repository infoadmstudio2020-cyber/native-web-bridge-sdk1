package com.roni.library.runtime;

import com.roni.library.runtime.server.LocalWebServer;
import com.roni.library.runtime.source.WebSource;
import com.roni.library.runtime.source.WebSourceResolver;
import org.junit.Assert;
import org.junit.Test;

public class WebServerAndInterceptorTest {

    @Test
    public void testLocalWebServerLifecycle() {
        WebSourceResolver resolver = new WebSourceResolver();
        LocalWebServer server = new LocalWebServer(8080, resolver);
        Assert.assertEquals(8080, server.getPort());
        Assert.assertFalse(server.isRunning());

        server.start();
        Assert.assertTrue(server.isRunning());

        WebSource src = server.handleRequest("/", "GET");
        Assert.assertTrue(src.isFound());

        server.stop();
        Assert.assertFalse(server.isRunning());

        WebSource stoppedSrc = server.handleRequest("/", "GET");
        Assert.assertFalse(stoppedSrc.isFound());
    }

    @Test
    public void testWebResourceInterceptor() {
        WebSourceResolver resolver = new WebSourceResolver();
        WebResourceInterceptor interceptor = new WebResourceInterceptor(resolver);

        Assert.assertTrue(interceptor.isAllowed("http://127.0.0.1:8080/index.html"));
        Assert.assertFalse(interceptor.isAllowed("http://malicious.external.com/hack"));

        interceptor.addAllowedOrigin("https://trusted.partner.com");
        Assert.assertTrue(interceptor.isAllowed("https://trusted.partner.com/sdk.js"));

        WebSource allowedSrc = interceptor.intercept("http://127.0.0.1:8080/");
        Assert.assertTrue(allowedSrc.isFound());

        WebSource blockedSrc = interceptor.intercept("http://blocked.org/file.js");
        Assert.assertFalse(blockedSrc.isFound());
    }
}
