package com.roni.library.devtools.diagnostics;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Collects and aggregates system health, bridge latency, and platform manifests.
 */
public class DiagnosticsManager {

    private final AtomicLong totalInvocations = new AtomicLong(0);
    private final AtomicLong successfulInvocations = new AtomicLong(0);
    private final AtomicLong failedInvocations = new AtomicLong(0);
    private final AtomicLong totalLatencyMs = new AtomicLong(0);
    private final Map<String, AtomicLong> pathCounts = new ConcurrentHashMap<>();

    public DiagnosticsManager() {}

    public void recordBridgeInvocation(String path, long latencyMs, boolean success) {
        totalInvocations.incrementAndGet();
        if (success) {
            successfulInvocations.incrementAndGet();
        } else {
            failedInvocations.incrementAndGet();
        }
        totalLatencyMs.addAndGet(Math.max(0, latencyMs));

        if (path != null) {
            pathCounts.computeIfAbsent(path, k -> new AtomicLong(0)).incrementAndGet();
        }
    }

    public Map<String, Object> collectServiceHealth() {
        Map<String, Object> health = new HashMap<>();
        long total = totalInvocations.get();
        double errorRate = total > 0 ? (double) failedInvocations.get() / total : 0.0;
        health.put("status", errorRate > 0.2 ? "DEGRADED" : "HEALTHY");
        health.put("totalCalls", total);
        health.put("errorRate", errorRate);
        health.put("uptimeMs", System.currentTimeMillis());
        return health;
    }

    public Map<String, Object> collectBridgeMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        long total = totalInvocations.get();
        double avgLatency = total > 0 ? (double) totalLatencyMs.get() / total : 0.0;
        metrics.put("totalInvocations", total);
        metrics.put("successfulInvocations", successfulInvocations.get());
        metrics.put("failedInvocations", failedInvocations.get());
        metrics.put("avgLatencyMs", avgLatency);

        Map<String, Long> paths = new HashMap<>();
        for (Map.Entry<String, AtomicLong> e : pathCounts.entrySet()) {
            paths.put(e.getKey(), e.getValue().get());
        }
        metrics.put("pathDistribution", paths);
        return metrics;
    }

    public Map<String, Object> generatePlatformManifest() {
        Map<String, Object> manifest = new HashMap<>();
        manifest.put("sdkName", "NativeDevToolsSDK");
        manifest.put("sdkVersion", "1.0.0");
        manifest.put("os", System.getProperty("os.name", "Android"));
        manifest.put("javaVersion", System.getProperty("java.version", "17"));
        manifest.put("timestamp", System.currentTimeMillis());
        return manifest;
    }
}
