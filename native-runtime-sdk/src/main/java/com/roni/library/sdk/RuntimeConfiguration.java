package com.roni.library.sdk;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Immutable configuration defining runtime environment, behavior bounds, and telemetry options.
 */
public final class RuntimeConfiguration implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String appId;
    private final String environment;
    private final boolean debugEnabled;
    private final String logLevel;
    private final boolean storageEncryptionEnabled;
    private final int networkTimeoutMs;
    private final int maxNetworkRetries;
    private final boolean telemetryEnabled;
    private final Map<String, String> customProperties;

    private RuntimeConfiguration(Builder builder) {
        this.appId = builder.appId;
        this.environment = builder.environment;
        this.debugEnabled = builder.debugEnabled;
        this.logLevel = builder.logLevel;
        this.storageEncryptionEnabled = builder.storageEncryptionEnabled;
        this.networkTimeoutMs = builder.networkTimeoutMs;
        this.maxNetworkRetries = builder.maxNetworkRetries;
        this.telemetryEnabled = builder.telemetryEnabled;
        this.customProperties = Collections.unmodifiableMap(new HashMap<>(builder.customProperties));
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getAppId() {
        return appId;
    }

    public String getEnvironment() {
        return environment;
    }

    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public boolean isStorageEncryptionEnabled() {
        return storageEncryptionEnabled;
    }

    public int getNetworkTimeoutMs() {
        return networkTimeoutMs;
    }

    public int getMaxNetworkRetries() {
        return maxNetworkRetries;
    }

    public boolean isTelemetryEnabled() {
        return telemetryEnabled;
    }

    public Map<String, String> getCustomProperties() {
        return customProperties;
    }

    public static final class Builder {
        private String appId = "com.roni.app.default";
        private String environment = "PRODUCTION";
        private boolean debugEnabled = false;
        private String logLevel = "INFO";
        private boolean storageEncryptionEnabled = true;
        private int networkTimeoutMs = 15000;
        private int maxNetworkRetries = 3;
        private boolean telemetryEnabled = true;
        private final Map<String, String> customProperties = new HashMap<>();

        public Builder appId(String appId) {
            if (appId != null && !appId.trim().isEmpty()) {
                this.appId = appId.trim();
            }
            return this;
        }

        public Builder environment(String environment) {
            if (environment != null && !environment.trim().isEmpty()) {
                this.environment = environment.trim().toUpperCase();
            }
            return this;
        }

        public Builder debugEnabled(boolean debugEnabled) {
            this.debugEnabled = debugEnabled;
            return this;
        }

        public Builder logLevel(String logLevel) {
            if (logLevel != null) {
                this.logLevel = logLevel.toUpperCase();
            }
            return this;
        }

        public Builder storageEncryptionEnabled(boolean enabled) {
            this.storageEncryptionEnabled = enabled;
            return this;
        }

        public Builder networkTimeoutMs(int timeoutMs) {
            this.networkTimeoutMs = Math.max(1000, Math.min(60000, timeoutMs));
            return this;
        }

        public Builder maxNetworkRetries(int retries) {
            this.maxNetworkRetries = Math.max(0, Math.min(10, retries));
            return this;
        }

        public Builder telemetryEnabled(boolean telemetryEnabled) {
            this.telemetryEnabled = telemetryEnabled;
            return this;
        }

        public Builder addCustomProperty(String key, String value) {
            if (key != null && value != null) {
                this.customProperties.put(key, value);
            }
            return this;
        }

        public RuntimeConfiguration build() {
            return new RuntimeConfiguration(this);
        }
    }
}
