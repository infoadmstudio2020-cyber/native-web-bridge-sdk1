package com.roni.library.contracts.platform;

import androidx.annotation.NonNull;
import java.io.Serializable;

/**
 * Universal Data Transfer Object for device battery status.
 *
 * @author infoadmstudio2020-cyber
 */
public final class BatteryStatusDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int level;
    private final boolean isCharging;
    private final String pluggedType;
    private final float temperature;

    public BatteryStatusDTO(int level, boolean isCharging, @NonNull String pluggedType, float temperature) {
        this.level = level;
        this.isCharging = isCharging;
        this.pluggedType = pluggedType != null ? pluggedType : "NONE";
        this.temperature = temperature;
    }

    public int getLevel() {
        return level;
    }

    public boolean isCharging() {
        return isCharging;
    }

    @NonNull
    public String getPluggedType() {
        return pluggedType;
    }

    public float getTemperature() {
        return temperature;
    }

    @NonNull
    @Override
    public String toString() {
        return "BatteryStatusDTO{level=" + level + "%, charging=" + isCharging + ", plugged='" + pluggedType + "', temp=" + temperature + "°C}";
    }
}
