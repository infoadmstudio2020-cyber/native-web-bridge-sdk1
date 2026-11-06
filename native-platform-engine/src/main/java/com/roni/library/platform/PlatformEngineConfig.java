package com.roni.library.platform;

/**
 * Configuration options for PlatformEngine.
 *
 * @author infoadmstudio2020-cyber
 */
public class PlatformEngineConfig {

    private final boolean debugEnabled;
    private final String tag;
    private final boolean autoMonitorNetwork;
    private final boolean autoMonitorBattery;

    private PlatformEngineConfig(Builder builder) {
        this.debugEnabled = builder.debugEnabled;
        this.tag = builder.tag != null ? builder.tag : "PlatformEngine";
        this.autoMonitorNetwork = builder.autoMonitorNetwork;
        this.autoMonitorBattery = builder.autoMonitorBattery;
    }

    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    public String getTag() {
        return tag;
    }

    public boolean isAutoMonitorNetwork() {
        return autoMonitorNetwork;
    }

    public boolean isAutoMonitorBattery() {
        return autoMonitorBattery;
    }

    public static class Builder {
        private boolean debugEnabled = false;
        private String tag = "PlatformEngine";
        private boolean autoMonitorNetwork = true;
        private boolean autoMonitorBattery = true;

        public Builder debugEnabled(boolean debugEnabled) {
            this.debugEnabled = debugEnabled;
            return this;
        }

        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder autoMonitorNetwork(boolean autoMonitorNetwork) {
            this.autoMonitorNetwork = autoMonitorNetwork;
            return this;
        }

        public Builder autoMonitorBattery(boolean autoMonitorBattery) {
            this.autoMonitorBattery = autoMonitorBattery;
            return this;
        }

        public PlatformEngineConfig build() {
            return new PlatformEngineConfig(this);
        }
    }
}
