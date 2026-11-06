package com.roni.library.theme;

/**
 * Immutable configuration for NativeThemeEngine.
 */
public class NativeThemeEngineConfig {

    private final boolean debugEnabled;
    private final String tag;
    private final int timeout;
    private final boolean autoDetectSystemTheme;

    private NativeThemeEngineConfig(Builder builder) {
        this.debugEnabled = builder.debugEnabled;
        this.tag = builder.tag;
        this.timeout = builder.timeout;
        this.autoDetectSystemTheme = builder.autoDetectSystemTheme;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static NativeThemeEngineConfig defaultConfiguration() {
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

    public boolean isAutoDetectSystemTheme() {
        return autoDetectSystemTheme;
    }

    public static class Builder {
        private boolean debugEnabled = false;
        private String tag = "NativeThemeEngine";
        private int timeout = 30000;
        private boolean autoDetectSystemTheme = true;

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

        public Builder autoDetectSystemTheme(boolean autoDetect) {
            this.autoDetectSystemTheme = autoDetect;
            return this;
        }

        public NativeThemeEngineConfig build() {
            return new NativeThemeEngineConfig(this);
        }
    }
}
