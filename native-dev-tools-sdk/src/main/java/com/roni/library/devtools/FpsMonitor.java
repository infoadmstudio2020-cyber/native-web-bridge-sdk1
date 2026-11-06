package com.roni.library.devtools;

import com.roni.library.devtools.profiler.MemoryMetrics;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Monitors live FPS, frame render latency, thread count, and JVM memory allocation.
 */
public class FpsMonitor {

    private final AtomicBoolean running = new AtomicBoolean(false);
    private volatile double currentFps = 60.0;
    private volatile double frameTimeMs = 16.6;

    public FpsMonitor() {}

    public void start() {
        running.set(true);
    }

    public void stop() {
        running.set(false);
    }

    public boolean isRunning() {
        return running.get();
    }

    public void updateFps(double fps) {
        this.currentFps = Math.max(0.0, fps);
        this.frameTimeMs = fps > 0.0 ? (1000.0 / fps) : 0.0;
    }

    public double getCurrentFps() {
        return currentFps;
    }

    public double getFrameTimeMs() {
        return frameTimeMs;
    }

    public int getThreadCount() {
        return Thread.activeCount();
    }

    public MemoryMetrics getMemorySnapshot() {
        return MemoryMetrics.capture();
    }

    public Map<String, Object> getTelemetrySnapshot() {
        Map<String, Object> map = new HashMap<>();
        map.put("fps", currentFps);
        map.put("frameTimeMs", frameTimeMs);
        map.put("activeThreads", getThreadCount());
        map.put("memory", getMemorySnapshot().toMap());
        map.put("running", running.get());
        return map;
    }
}
