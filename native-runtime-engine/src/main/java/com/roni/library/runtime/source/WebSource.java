package com.roni.library.runtime.source;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a resolved web resource (HTML, JS, CSS, JSON, Image).
 */
public class WebSource implements Serializable {

    private final String url;
    private final String mimeType;
    private final byte[] data;
    private final int statusCode;
    private final Map<String, String> headers;

    public WebSource(String url, String mimeType, byte[] data, int statusCode, Map<String, String> headers) {
        this.url = url != null ? url : "";
        this.mimeType = mimeType != null ? mimeType : "text/html";
        this.data = data != null ? data : new byte[0];
        this.statusCode = statusCode;
        this.headers = headers != null ? new HashMap<>(headers) : new HashMap<>();
    }

    public static WebSource ofText(String url, String content, String mimeType) {
        byte[] bytes = content != null ? content.getBytes() : new byte[0];
        Map<String, String> h = new HashMap<>();
        h.put("Content-Type", mimeType);
        return new WebSource(url, mimeType, bytes, 200, h);
    }

    public static WebSource notFound(String url) {
        Map<String, String> h = new HashMap<>();
        h.put("Content-Type", "text/plain");
        return new WebSource(url, "text/plain", "404 Not Found".getBytes(), 404, h);
    }

    public String getUrl() {
        return url;
    }

    public String getMimeType() {
        return mimeType;
    }

    public byte[] getData() {
        return data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Map<String, String> getHeaders() {
        return new HashMap<>(headers);
    }

    public long getContentLength() {
        return data.length;
    }

    public boolean isFound() {
        return statusCode >= 200 && statusCode < 400;
    }
}
