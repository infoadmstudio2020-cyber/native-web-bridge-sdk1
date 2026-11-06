package com.roni.library.devtools.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Exports network traffic records into standard HTTP Archive (HAR 1.2) structure.
 */
public class HarExporter {

    public Map<String, Object> exportHar(List<HttpTrafficRecord> records) {
        Map<String, Object> root = new HashMap<>();
        Map<String, Object> log = new HashMap<>();

        log.put("version", "1.2");

        Map<String, String> creator = new HashMap<>();
        creator.put("name", "NativeDevToolsSDK");
        creator.put("version", "1.0.0");
        log.put("creator", creator);

        List<Map<String, Object>> entries = new ArrayList<>();
        if (records != null) {
            for (HttpTrafficRecord record : records) {
                entries.add(createHarEntry(record));
            }
        }
        log.put("entries", entries);
        root.put("log", log);
        return root;
    }

    private Map<String, Object> createHarEntry(HttpTrafficRecord record) {
        Map<String, Object> entry = new HashMap<>();
        entry.put("startedDateTime", String.valueOf(record.getTimestamp()));
        entry.put("time", record.getLatencyMs());

        Map<String, Object> req = new HashMap<>();
        req.put("method", record.getMethod());
        req.put("url", record.getUrl());
        req.put("httpVersion", "HTTP/1.1");
        req.put("headers", mapToHeaderList(record.getRequestHeaders()));
        Map<String, Object> postData = new HashMap<>();
        postData.put("mimeType", "application/json");
        postData.put("text", record.getRequestBody());
        req.put("postData", postData);
        entry.put("request", req);

        Map<String, Object> res = new HashMap<>();
        res.put("status", record.getStatusCode());
        res.put("statusText", record.getStatusCode() == 200 ? "OK" : "Status " + record.getStatusCode());
        res.put("httpVersion", "HTTP/1.1");
        res.put("headers", mapToHeaderList(record.getResponseHeaders()));
        Map<String, Object> content = new HashMap<>();
        content.put("size", record.getResponseBody().length());
        content.put("mimeType", "application/json");
        content.put("text", record.getResponseBody());
        res.put("content", content);
        entry.put("response", res);

        Map<String, Object> timings = new HashMap<>();
        timings.put("send", 0);
        timings.put("wait", record.getLatencyMs());
        timings.put("receive", 0);
        entry.put("timings", timings);

        return entry;
    }

    private List<Map<String, String>> mapToHeaderList(Map<String, String> map) {
        List<Map<String, String>> list = new ArrayList<>();
        if (map != null) {
            for (Map.Entry<String, String> e : map.entrySet()) {
                Map<String, String> item = new HashMap<>();
                item.put("name", e.getKey());
                item.put("value", e.getValue());
                list.add(item);
            }
        }
        return list;
    }
}
