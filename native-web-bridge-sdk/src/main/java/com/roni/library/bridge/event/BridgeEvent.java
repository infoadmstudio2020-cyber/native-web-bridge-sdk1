package com.roni.library.bridge.event;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Event envelope broadcasted through the bridge event bus.
 */
public final class BridgeEvent {

    private final String topic;
    private final Map<String, Object> data;
    private final String source;
    private final long timestamp;

    public BridgeEvent(String topic, Map<String, Object> data, String source, long timestamp) {
        this.topic = topic != null ? topic : "";
        this.data = data != null ? Collections.unmodifiableMap(new HashMap<>(data)) : Collections.emptyMap();
        this.source = source != null ? source : "native";
        this.timestamp = timestamp > 0 ? timestamp : System.currentTimeMillis();
    }

    public static BridgeEvent of(String topic, Map<String, Object> data) {
        return new BridgeEvent(topic, data, "native", System.currentTimeMillis());
    }

    public static BridgeEvent of(String topic, Map<String, Object> data, String source) {
        return new BridgeEvent(topic, data, source, System.currentTimeMillis());
    }

    public String getTopic() {
        return topic;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public String getSource() {
        return source;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "BridgeEvent{" +
                "topic='" + topic + '\'' +
                ", data=" + data +
                ", source='" + source + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
