package com.roni.library.devtools.profiler;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Snapshot of JVM and runtime memory metrics.
 */
public class MemoryMetrics implements Serializable {

    private final long usedHeapBytes;
    private final long totalHeapBytes;
    private final long maxHeapBytes;
    private final long freeHeapBytes;
    private final long gcCount;
    private final long nativeAllocatedBytes;
    private final long timestamp;

    public MemoryMetrics(long usedHeapBytes,
                         long totalHeapBytes,
                         long maxHeapBytes,
                         long freeHeapBytes,
                         long gcCount,
                         long nativeAllocatedBytes,
                         long timestamp) {
        this.usedHeapBytes = usedHeapBytes;
        this.totalHeapBytes = totalHeapBytes;
        this.maxHeapBytes = maxHeapBytes;
        this.freeHeapBytes = freeHeapBytes;
        this.gcCount = gcCount;
        this.nativeAllocatedBytes = nativeAllocatedBytes;
        this.timestamp = timestamp;
    }

    public static MemoryMetrics capture() {
        Runtime runtime = Runtime.getRuntime();
        long total = runtime.totalMemory();
        long free = runtime.freeMemory();
        long max = runtime.maxMemory();
        long used = total - free;

        return new MemoryMetrics(
                used,
                total,
                max,
                free,
                0,
                0,
                System.currentTimeMillis()
        );
    }

    public long getUsedHeapBytes() {
        return usedHeapBytes;
    }

    public long getTotalHeapBytes() {
        return totalHeapBytes;
    }

    public long getMaxHeapBytes() {
        return maxHeapBytes;
    }

    public long getFreeHeapBytes() {
        return freeHeapBytes;
    }

    public long getGcCount() {
        return gcCount;
    }

    public long getNativeAllocatedBytes() {
        return nativeAllocatedBytes;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("usedHeapBytes", usedHeapBytes);
        map.put("totalHeapBytes", totalHeapBytes);
        map.put("maxHeapBytes", maxHeapBytes);
        map.put("freeHeapBytes", freeHeapBytes);
        map.put("gcCount", gcCount);
        map.put("nativeAllocatedBytes", nativeAllocatedBytes);
        map.put("timestamp", timestamp);
        return map;
    }
}
