package com.roni.library.router;

/**
 * Configuration for NativeRouterSDK.
 *
 * @author infoadmstudio2020-cyber
 */
public class NativeRouterSDKConfig {

    private final boolean debugEnabled;
    private final String tag;
    private final int timeout;

    private NativeRouterSDKConfig(Builder builder) {
        this.debugEnabled = builder.debugEnabled;
        this.tag = builder.tag != null ? builder.tag : "NativeRouterSDK";
        this.timeout = builder.timeout > 0 ? builder.timeout : 30000;
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

    public static class Builder {
        private boolean debugEnabled = false;
        private String tag = "NativeRouterSDK";
        private int timeout = 30000;

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

        public NativeRouterSDKConfig build() {
            return new NativeRouterSDKConfig(this);
        }
    }
}
