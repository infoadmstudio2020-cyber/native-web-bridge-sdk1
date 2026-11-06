package com.roni.library.devtools.network;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Immutable representation of an HTTP/WebSocket network transaction.
 */
public class HttpTrafficRecord implements Serializable {

    private final String requestId;
    private final String url;
    private final String method;
    private final int statusCode;
    private final Map<String, String> requestHeaders;
    private final Map<String, String> responseHeaders;
    private final String requestBody;
    private final String responseBody;
    private final long latencyMs;
    private final long timestamp;

    public HttpTrafficRecord(String requestId,
                             String url,
                             String method,
                             int statusCode,
                             Map<String, String> requestHeaders,
                             Map<String, String> responseHeaders,
                             String requestBody,
                             String responseBody,
                             long latencyMs,
                             long timestamp) {
        this.requestId = requestId != null ? requestId : String.valueOf(System.currentTimeMillis());
        this.url = url != null ? url : "";
        this.method = method != null ? method : "GET";
        this.statusCode = statusCode;
        this.requestHeaders = requestHeaders != null ? new HashMap<>(requestHeaders) : new HashMap<>();
        this.responseHeaders = responseHeaders != null ? new HashMap<>(responseHeaders) : new HashMap<>();
        this.requestBody = requestBody != null ? requestBody : "";
        this.responseBody = responseBody != null ? responseBody : "";
        this.latencyMs = latencyMs;
        this.timestamp = timestamp;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Map<String, String> getRequestHeaders() {
        return new HashMap<>(requestHeaders);
    }

    public Map<String, String> getResponseHeaders() {
        return new HashMap<>(responseHeaders);
    }

    public String getRequestBody() {
        return requestBody;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public long getLatencyMs() {
        return latencyMs;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("requestId", requestId);
        map.put("url", url);
        map.put("method", method);
        map.put("statusCode", statusCode);
        map.put("requestHeaders", new HashMap<>(requestHeaders));
        map.put("responseHeaders", new HashMap<>(responseHeaders));
        map.put("requestBody", requestBody);
        map.put("responseBody", responseBody);
        map.put("latencyMs", latencyMs);
        map.put("timestamp", timestamp);
        return map;
    }
}
