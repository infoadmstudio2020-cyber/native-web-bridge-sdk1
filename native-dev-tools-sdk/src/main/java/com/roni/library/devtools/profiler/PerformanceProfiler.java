package com.roni.library.devtools.profiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Tracks runtime execution spans, CPU metrics, and frame rate estimates.
 */
public class PerformanceProfiler {

    private volatile boolean recording = false;
    private final List<PerformanceSpan> recordedSpans = new CopyOnWriteArrayList<>();
    private volatile double cpuUsage = 0.0;
    private volatile double currentFps = 60.0;
    private volatile double frameTimeMs = 16.6;

    public PerformanceProfiler() {}

    public boolean isRecording() {
        return recording;
    }

    public synchronized void startRecording() {
        this.recording = true;
    }

    public synchronized void stopRecording() {
        this.recording = false;
    }

    public void clear() {
        recordedSpans.clear();
    }

    public void updateMetrics(double cpu, double fpsVal, double frameTimeMs) {
        this.cpuUsage = cpu;
        this.currentFps = fpsVal;
        this.frameTimeMs = frameTimeMs;
    }

    public PerformanceSpan startSpan(String name, String category) {
        long nowUs = System.nanoTime() / 1000L;
        return new PerformanceSpan(name, category, nowUs);
    }

    public void recordSpan(PerformanceSpan span) {
        if (span != null && recording) {
            recordedSpans.add(span);
        }
    }

    public List<PerformanceSpan> getRecordedSpans() {
        return new ArrayList<>(recordedSpans);
    }

    public Map<String, Object> getMetricsSnapshot() {
        Map<String, Object> map = new HashMap<>();
        map.put("recording", recording);
        map.put("cpuUsage", cpuUsage);
        map.put("currentFps", currentFps);
        map.put("frameTimeMs", frameTimeMs);
        map.put("spanCount", recordedSpans.size());
        map.put("memory", MemoryMetrics.capture().toMap());
        return map;
    }
}
