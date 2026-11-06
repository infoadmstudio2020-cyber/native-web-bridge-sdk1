package com.roni.library.security;

/**
 * Immutable configuration settings for {@link SecurityEngine}.
 *
 * @author infoadmstudio2020-cyber
 */
public final class SecurityEngineConfig {

    private final boolean debugEnabled;
    private final String tag;
    private final boolean requireHardwareBackedKeys;
    private final boolean biometricFallbackEnabled;

    private SecurityEngineConfig(Builder builder) {
        this.debugEnabled = builder.debugEnabled;
        this.tag = builder.tag;
        this.requireHardwareBackedKeys = builder.requireHardwareBackedKeys;
        this.biometricFallbackEnabled = builder.biometricFallbackEnabled;
    }

    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    public String getTag() {
        return tag;
    }

    public boolean isRequireHardwareBackedKeys() {
        return requireHardwareBackedKeys;
    }

    public boolean isBiometricFallbackEnabled() {
        return biometricFallbackEnabled;
    }

    public static class Builder {
        private boolean debugEnabled = false;
        private String tag = "NativeSecurityEngine";
        private boolean requireHardwareBackedKeys = false;
        private boolean biometricFallbackEnabled = true;

        public Builder debugEnabled(boolean debugEnabled) {
            this.debugEnabled = debugEnabled;
            return this;
        }

        public Builder tag(String tag) {
            if (tag != null && !tag.trim().isEmpty()) {
                this.tag = tag;
            }
            return this;
        }

        public Builder requireHardwareBackedKeys(boolean require) {
            this.requireHardwareBackedKeys = require;
            return this;
        }

        public Builder biometricFallbackEnabled(boolean enabled) {
            this.biometricFallbackEnabled = enabled;
            return this;
        }

        public SecurityEngineConfig build() {
            return new SecurityEngineConfig(this);
        }
    }
}
