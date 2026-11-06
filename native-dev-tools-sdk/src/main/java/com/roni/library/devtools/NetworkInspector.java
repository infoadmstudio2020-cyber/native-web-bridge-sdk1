package com.roni.library.devtools;

import com.roni.library.devtools.network.HttpTrafficRecord;
import com.roni.library.devtools.network.NetworkTrafficInspector;
import java.util.List;
import java.util.Map;

/**
 * Public facade for in-app HTTP/API network inspection and traffic snooping.
 */
public class NetworkInspector {

    private final NetworkTrafficInspector inspector;

    public NetworkInspector() {
        this(new NetworkTrafficInspector());
    }

    public NetworkInspector(NetworkTrafficInspector inspector) {
        this.inspector = inspector != null ? inspector : new NetworkTrafficInspector();
    }

    public boolean isEnabled() {
        return inspector.isEnabled();
    }

    public void setEnabled(boolean enabled) {
        inspector.setEnabled(enabled);
    }

    public void record(String url, String method, int statusCode,
                       Map<String, String> requestHeaders,
                       Map<String, String> responseHeaders,
                       String requestBody,
                       String responseBody,
                       long latencyMs) {
        HttpTrafficRecord record = new HttpTrafficRecord(
                String.valueOf(System.currentTimeMillis()),
                url,
                method,
                statusCode,
                requestHeaders,
                responseHeaders,
                requestBody,
                responseBody,
                latencyMs,
                System.currentTimeMillis()
        );
        inspector.recordTraffic(record);
    }

    public List<HttpTrafficRecord> getRecords() {
        return inspector.getTrafficHistory();
    }

    public void clear() {
        inspector.clear();
    }

    public Map<String, Object> exportHar() {
        return inspector.exportHar();
    }

    public NetworkTrafficInspector getUnderlyingInspector() {
        return inspector;
    }
}
