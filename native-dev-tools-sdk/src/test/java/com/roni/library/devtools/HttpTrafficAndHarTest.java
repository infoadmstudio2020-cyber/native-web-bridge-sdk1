package com.roni.library.devtools;

import com.roni.library.devtools.network.HarExporter;
import com.roni.library.devtools.network.HttpTrafficRecord;
import org.junit.Assert;
import org.junit.Test;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HttpTrafficAndHarTest {

    @Test
    public void testHttpTrafficRecord() {
        Map<String, String> reqHeaders = new HashMap<>();
        reqHeaders.put("Authorization", "Bearer token123");
        Map<String, String> resHeaders = new HashMap<>();
        resHeaders.put("Content-Type", "application/json");

        HttpTrafficRecord record = new HttpTrafficRecord(
                "req-99",
                "https://api.example.com/user",
                "POST",
                201,
                reqHeaders,
                resHeaders,
                "{\"name\":\"Dev\"}",
                "{\"id\":1}",
                45,
                System.currentTimeMillis()
        );

        Assert.assertEquals("req-99", record.getRequestId());
        Assert.assertEquals("https://api.example.com/user", record.getUrl());
        Assert.assertEquals("POST", record.getMethod());
        Assert.assertEquals(201, record.getStatusCode());
        Assert.assertEquals(45, record.getLatencyMs());

        Map<String, Object> map = record.toMap();
        Assert.assertEquals(201, map.get("statusCode"));
        Assert.assertEquals("POST", map.get("method"));
    }

    @Test
    public void testHarExporter() {
        HttpTrafficRecord record = new HttpTrafficRecord(
                "req-1",
                "https://api.example.com/data",
                "GET",
                200,
                null,
                null,
                "",
                "{}",
                20,
                System.currentTimeMillis()
        );

        HarExporter exporter = new HarExporter();
        Map<String, Object> har = exporter.exportHar(Collections.singletonList(record));

        Assert.assertNotNull(har);
        Assert.assertTrue(har.containsKey("log"));
        Map<String, Object> log = (Map<String, Object>) har.get("log");
        Assert.assertEquals("1.2", log.get("version"));
    }
}
