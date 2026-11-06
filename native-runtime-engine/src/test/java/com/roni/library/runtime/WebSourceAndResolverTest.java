package com.roni.library.runtime;

import com.roni.library.runtime.source.WebSource;
import com.roni.library.runtime.source.WebSourceResolver;
import org.junit.Assert;
import org.junit.Test;

public class WebSourceAndResolverTest {

    @Test
    public void testWebSourceCreation() {
        WebSource source = WebSource.ofText("/app/index.html", "<html>Hello</html>", "text/html");
        Assert.assertEquals("/app/index.html", source.getUrl());
        Assert.assertEquals("text/html", source.getMimeType());
        Assert.assertEquals(200, source.getStatusCode());
        Assert.assertTrue(source.isFound());
        Assert.assertTrue(source.getContentLength() > 0);

        WebSource notFound = WebSource.notFound("/missing");
        Assert.assertEquals(404, notFound.getStatusCode());
        Assert.assertFalse(notFound.isFound());
    }

    @Test
    public void testWebSourceResolverRoutes() {
        WebSourceResolver resolver = new WebSourceResolver();
        Assert.assertTrue(resolver.getRouteCount() >= 2);

        WebSource home = resolver.resolve("/");
        Assert.assertTrue(home.isFound());

        WebSource custom = WebSource.ofText("/data.json", "{\"status\":1}", "application/json");
        resolver.registerRoute("/data.json", custom);

        WebSource resolved = resolver.resolve("http://127.0.0.1:8080/data.json?v=123");
        Assert.assertTrue(resolved.isFound());
        Assert.assertEquals("application/json", resolved.getMimeType());

        resolver.unregisterRoute("/data.json");
        WebSource unreg = resolver.resolve("/data.json");
        Assert.assertFalse(unreg.isFound());

        resolver.clear();
        Assert.assertEquals(0, resolver.getRouteCount());
    }
}
