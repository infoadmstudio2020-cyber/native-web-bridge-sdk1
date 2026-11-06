package com.roni.library.devtools;

import com.roni.library.devtools.network.HttpTrafficRecord;
import com.roni.library.devtools.network.NetworkTrafficInspector;
import org.junit.Assert;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class NetworkInspectorTest {

    @Test
    public void testNetworkInspectorAndMocking() {
        NetworkInspector inspector = new NetworkInspector();
        Assert.assertTrue(inspector.isEnabled());

        // Register Breakpoint / Mock
        inspector.getUnderlyingInspector().registerBreakpoint("/mock-test", new NetworkTrafficInspector.MockInjector() {
            @Override
            public HttpTrafficRecord inject(String url, String method, Map<String, String> headers, String body) {
                return new HttpTrafficRecord("mock-1", url, method, 200, headers, new HashMap<>(), body, "{\"mocked\":true}", 5, System.currentTimeMillis());
            }
        });

        HttpTrafficRecord mockResult = inspector.getUnderlyingInspector().checkBreakpoint("https://api.test/mock-test", "GET", new HashMap<>(), "");
        Assert.assertNotNull(mockResult);
        Assert.assertEquals(200, mockResult.getStatusCode());
        Assert.assertEquals("{\"mocked\":true}", mockResult.getResponseBody());

        // Record normal traffic
        inspector.record("https://api.test/users", "GET", 200, new HashMap<>(), new HashMap<>(), "", "[]", 15);
        Assert.assertEquals(1, inspector.getRecords().size());

        Map<String, Object> har = inspector.exportHar();
        Assert.assertNotNull(har);

        inspector.clear();
        Assert.assertEquals(0, inspector.getRecords().size());
    }
}
