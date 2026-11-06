package com.roni.library.platform.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Record representing a dispatched toast notification.
 *
 * @author infoadmstudio2020-cyber
 */
public class ToastRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String message;
    private final boolean isLong;
    private final long timestamp;

    public ToastRecord(String message, boolean isLong, long timestamp) {
        this.message = message != null ? message : "";
        this.isLong = isLong;
        this.timestamp = timestamp > 0 ? timestamp : System.currentTimeMillis();
    }

    public String getMessage() {
        return message;
    }

    public boolean isLong() {
        return isLong;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("message", message);
        map.put("isLong", isLong);
        map.put("timestamp", timestamp);
        return map;
    }

    @Override
    public String toString() {
        return "ToastRecord{" +
                "message='" + message + '\'' +
                ", isLong=" + isLong +
                ", timestamp=" + timestamp +
                '}';
    }
}
