package com.roni.library.platform;

import java.util.HashMap;
import java.util.Map;

/**
 * Sensor hub abstraction providing unified sensor and hardware telemetry bridge.
 *
 * @author infoadmstudio2020-cyber
 */
public class SensorHub {

    private static final Map<String, Boolean> SENSOR_AVAILABILITY = new HashMap<>();

    static {
        SENSOR_AVAILABILITY.put("accelerometer", true);
        SENSOR_AVAILABILITY.put("gyroscope", true);
        SENSOR_AVAILABILITY.put("magnetometer", true);
        SENSOR_AVAILABILITY.put("light", true);
        SENSOR_AVAILABILITY.put("proximity", true);
        SENSOR_AVAILABILITY.put("step_counter", true);
    }

    public static boolean isSensorAvailable(String sensorName) {
        if (sensorName == null) return false;
        Boolean available = SENSOR_AVAILABILITY.get(sensorName.toLowerCase());
        return available != null && available;
    }

    public static Map<String, Object> getSensorTelemetry() {
        Map<String, Object> map = new HashMap<>();
        map.put("availableSensors", new HashMap<>(SENSOR_AVAILABILITY));
        map.put("totalSensors", SENSOR_AVAILABILITY.size());
        map.put("timestamp", System.currentTimeMillis());
        return map;
    }
}
