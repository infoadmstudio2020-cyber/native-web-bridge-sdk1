package com.roni.library.devtools.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Inspects, captures, mocks, and exports HTTP and API network traffic.
 */
public class NetworkTrafficInspector {

    public interface MockInjector {
        HttpTrafficRecord inject(String url, String method, Map<String, String> headers, String body);
    }

    private volatile boolean enabled = true;
    private final List<HttpTrafficRecord> trafficHistory = new CopyOnWriteArrayList<>();
    private final Map<String, MockInjector> breakpoints = new ConcurrentHashMap<>();
    private final HarExporter harExporter = new HarExporter();

    public NetworkTrafficInspector() {}

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void registerBreakpoint(String urlPattern, MockInjector injector) {
        if (urlPattern != null && injector != null) {
            breakpoints.put(urlPattern, injector);
        }
    }

    public void removeBreakpoint(String urlPattern) {
        if (urlPattern != null) {
            breakpoints.remove(urlPattern);
        }
    }

    public HttpTrafficRecord checkBreakpoint(String url, String method, Map<String, String> headers, String body) {
        if (!enabled || url == null) {
            return null;
        }
        for (Map.Entry<String, MockInjector> entry : breakpoints.entrySet()) {
            if (url.contains(entry.getKey())) {
                try {
                    return entry.getValue().inject(url, method, headers, body);
                } catch (Throwable ignored) {}
            }
        }
        return null;
    }

    public void recordTraffic(HttpTrafficRecord record) {
        if (enabled && record != null) {
            trafficHistory.add(record);
            if (trafficHistory.size() > 500) {
                trafficHistory.remove(0);
            }
        }
    }

    public List<HttpTrafficRecord> getTrafficHistory() {
        return new ArrayList<>(trafficHistory);
    }

    public void clear() {
        trafficHistory.clear();
    }

    public Map<String, Object> exportHar() {
        return harExporter.exportHar(new ArrayList<>(trafficHistory));
    }
}
