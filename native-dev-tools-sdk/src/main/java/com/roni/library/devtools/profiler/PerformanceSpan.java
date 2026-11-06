package com.roni.library.devtools.profiler;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a timed performance profiling span.
 */
public class PerformanceSpan implements Serializable {

    private final String name;
    private final String category;
    private final long startTimeUs;
    private volatile long durationUs;
    private final Map<String, Object> metadata = new HashMap<>();

    public PerformanceSpan(String name, String category, long startTimeUs) {
        this.name = name != null ? name : "unnamed";
        this.category = category != null ? category : "default";
        this.startTimeUs = startTimeUs;
        this.durationUs = 0;
    }

    public void stop(long endTimeUs) {
        if (endTimeUs >= startTimeUs) {
            this.durationUs = endTimeUs - startTimeUs;
        } else {
            this.durationUs = 0;
        }
    }

    public void addMetadata(String key, Object value) {
        if (key != null && value != null) {
            metadata.put(key, value);
        }
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public long getStartTimeUs() {
        return startTimeUs;
    }

    public long getDurationUs() {
        return durationUs;
    }

    public Map<String, Object> getMetadata() {
        return new HashMap<>(metadata);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("category", category);
        map.put("startTimeUs", startTimeUs);
        map.put("durationUs", durationUs);
        map.put("metadata", new HashMap<>(metadata));
        return map;
    }
}
