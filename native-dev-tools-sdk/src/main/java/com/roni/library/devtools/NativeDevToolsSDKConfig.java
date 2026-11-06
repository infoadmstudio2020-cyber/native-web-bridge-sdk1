package com.roni.library.devtools;

/**
 * Configuration options for NativeDevToolsSDK.
 */
public class NativeDevToolsSDKConfig {

    private final boolean debugEnabled;
    private final String tag;
    private final int timeout;
    private final int serverPort;
    private final boolean floatingBubbleEnabled;
    private final boolean shakeDetectionEnabled;

    private NativeDevToolsSDKConfig(Builder builder) {
        this.debugEnabled = builder.debugEnabled;
        this.tag = builder.tag;
        this.timeout = builder.timeout;
        this.serverPort = builder.serverPort;
        this.floatingBubbleEnabled = builder.floatingBubbleEnabled;
        this.shakeDetectionEnabled = builder.shakeDetectionEnabled;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static NativeDevToolsSDKConfig defaultConfiguration() {
        return new Builder().build();
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

    public int getServerPort() {
        return serverPort;
    }

    public boolean isFloatingBubbleEnabled() {
        return floatingBubbleEnabled;
    }

    public boolean isShakeDetectionEnabled() {
        return shakeDetectionEnabled;
    }

    public static class Builder {
        private boolean debugEnabled = false;
        private String tag = "NativeDevToolsSDK";
        private int timeout = 30000;
        private int serverPort = 9222;
        private boolean floatingBubbleEnabled = true;
        private boolean shakeDetectionEnabled = true;

        public Builder() {}

        public Builder debugEnabled(boolean debugEnabled) {
            this.debugEnabled = debugEnabled;
            return this;
        }

        public Builder tag(String tag) {
            if (tag != null && !tag.trim().isEmpty()) {
                this.tag = tag.trim();
            }
            return this;
        }

        public Builder timeout(int timeout) {
            if (timeout > 0) {
                this.timeout = timeout;
            }
            return this;
        }

        public Builder serverPort(int serverPort) {
            if (serverPort > 0 && serverPort <= 65535) {
                this.serverPort = serverPort;
            }
            return this;
        }

        public Builder floatingBubbleEnabled(boolean floatingBubbleEnabled) {
            this.floatingBubbleEnabled = floatingBubbleEnabled;
            return this;
        }

        public Builder shakeDetectionEnabled(boolean shakeDetectionEnabled) {
            this.shakeDetectionEnabled = shakeDetectionEnabled;
            return this;
        }

        public NativeDevToolsSDKConfig build() {
            return new NativeDevToolsSDKConfig(this);
        }
    }
}
