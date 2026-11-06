package com.roni.library.runtime;

/**
 * Configuration for NativeRuntimeEngine instances.
 */
public class NativeRuntimeEngineConfig {

    private final boolean debugEnabled;
    private final String tag;
    private final int timeout;
    private final int localServerPort;
    private final boolean cspStrictEnabled;
    private final boolean allowMixedContent;
    private final boolean hardwareAccelerationEnabled;
    private final String defaultUrl;

    private NativeRuntimeEngineConfig(Builder builder) {
        this.debugEnabled = builder.debugEnabled;
        this.tag = builder.tag != null ? builder.tag : "NativeRuntimeEngine";
        this.timeout = builder.timeout > 0 ? builder.timeout : 30000;
        this.localServerPort = builder.localServerPort > 0 ? builder.localServerPort : 8080;
        this.cspStrictEnabled = builder.cspStrictEnabled;
        this.allowMixedContent = builder.allowMixedContent;
        this.hardwareAccelerationEnabled = builder.hardwareAccelerationEnabled;
        this.defaultUrl = builder.defaultUrl != null ? builder.defaultUrl : "http://127.0.0.1:8080/";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static NativeRuntimeEngineConfig defaultConfiguration() {
        return builder().build();
    }

    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    public String getTag() {
        return tag;
    }

    public int getTimeout() {
        return timeout;
    }

    public int getLocalServerPort() {
        return localServerPort;
    }

    public boolean isCspStrictEnabled() {
        return cspStrictEnabled;
    }

    public boolean isAllowMixedContent() {
        return allowMixedContent;
    }

    public boolean isHardwareAccelerationEnabled() {
        return hardwareAccelerationEnabled;
    }

    public String getDefaultUrl() {
        return defaultUrl;
    }

    public static class Builder {
        private boolean debugEnabled = false;
        private String tag = "NativeRuntimeEngine";
        private int timeout = 30000;
        private int localServerPort = 8080;
        private boolean cspStrictEnabled = true;
        private boolean allowMixedContent = false;
        private boolean hardwareAccelerationEnabled = true;
        private String defaultUrl = "http://127.0.0.1:8080/";

        public Builder debugEnabled(boolean debugEnabled) {
            this.debugEnabled = debugEnabled;
            return this;
        }

        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder timeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder localServerPort(int localServerPort) {
            this.localServerPort = localServerPort;
            return this;
        }

        public Builder cspStrictEnabled(boolean cspStrictEnabled) {
            this.cspStrictEnabled = cspStrictEnabled;
            return this;
        }

        public Builder allowMixedContent(boolean allowMixedContent) {
            this.allowMixedContent = allowMixedContent;
            return this;
        }

        public Builder hardwareAccelerationEnabled(boolean hardwareAccelerationEnabled) {
            this.hardwareAccelerationEnabled = hardwareAccelerationEnabled;
            return this;
        }

        public Builder defaultUrl(String defaultUrl) {
            this.defaultUrl = defaultUrl;
            return this;
        }

        public NativeRuntimeEngineConfig build() {
            return new NativeRuntimeEngineConfig(this);
        }
    }
}
