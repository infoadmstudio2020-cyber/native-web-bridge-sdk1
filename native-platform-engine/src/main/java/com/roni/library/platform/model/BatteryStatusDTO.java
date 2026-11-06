package com.roni.library.platform.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Data Transfer Object representing battery telemetry status.
 *
 * @author infoadmstudio2020-cyber
 */
public class BatteryStatusDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int level;
    private final boolean charging;
    private final String plugType;
    private final float temperature;
    private final String healthStatus;
    private final long timestamp;

    public BatteryStatusDTO(int level, boolean charging, String plugType, float temperature, String healthStatus, long timestamp) {
        this.level = Math.max(0, Math.min(100, level));
        this.charging = charging;
        this.plugType = plugType != null ? plugType : "UNKNOWN";
        this.temperature = temperature;
        this.healthStatus = healthStatus != null ? healthStatus : "GOOD";
        this.timestamp = timestamp > 0 ? timestamp : System.currentTimeMillis();
    }

    public static BatteryStatusDTO createDefault() {
        return new BatteryStatusDTO(85, false, "BATTERY", 29.5f, "GOOD", System.currentTimeMillis());
    }

    public int getLevel() {
        return level;
    }

    public boolean isCharging() {
        return charging;
    }

    public String getPlugType() {
        return plugType;
    }

    public float getTemperature() {
        return temperature;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("level", level);
        map.put("charging", charging);
        map.put("plugType", plugType);
        map.put("temperature", temperature);
        map.put("healthStatus", healthStatus);
        map.put("timestamp", timestamp);
        return map;
    }

    @Override
    public String toString() {
        return "BatteryStatusDTO{" +
                "level=" + level +
                ", charging=" + charging +
                ", plugType='" + plugType + '\'' +
                ", temperature=" + temperature +
                ", healthStatus='" + healthStatus + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
