package com.roni.library.bridge;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Configuration options for the NativeWebBridgeSDK instance.
 */
public final class BridgeConfig {

    private final String interfaceName;
    private final Set<String> allowedOrigins;
    private final long timeoutMs;
    private final boolean debugLogging;

    private BridgeConfig(Builder builder) {
        this.interfaceName = builder.interfaceName != null ? builder.interfaceName : "NativeBridge";
        this.allowedOrigins = Collections.unmodifiableSet(new HashSet<>(builder.allowedOrigins));
        this.timeoutMs = builder.timeoutMs > 0 ? builder.timeoutMs : 10000L;
        this.debugLogging = builder.debugLogging;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static BridgeConfig defaultConfig() {
        return builder().build();
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public Set<String> getAllowedOrigins() {
        return allowedOrigins;
    }

    public long getTimeoutMs() {
        return timeoutMs;
    }

    public boolean isDebugLogging() {
        return debugLogging;
    }

    public boolean isOriginAllowed(String origin) {
        if (allowedOrigins.isEmpty()) {
            return true;
        }
        if (origin == null) {
            return false;
        }
        return allowedOrigins.contains(origin) || allowedOrigins.contains("*");
    }

    public static final class Builder {
        private String interfaceName = "NativeBridge";
        private final Set<String> allowedOrigins = new HashSet<>();
        private long timeoutMs = 10000L;
        private boolean debugLogging = false;

        public Builder interfaceName(String name) {
            if (name != null && !name.trim().isEmpty()) {
                this.interfaceName = name.trim();
            }
            return this;
        }

        public Builder addAllowedOrigin(String origin) {
            if (origin != null && !origin.trim().isEmpty()) {
                this.allowedOrigins.add(origin.trim());
            }
            return this;
        }

        public Builder timeoutMs(long timeoutMs) {
            this.timeoutMs = timeoutMs;
            return this;
        }

        public Builder debugLogging(boolean enable) {
            this.debugLogging = enable;
            return this;
        }

        public BridgeConfig build() {
            return new BridgeConfig(this);
        }
    }
}
