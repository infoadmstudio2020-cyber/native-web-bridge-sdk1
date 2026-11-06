package com.roni.library.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.roni.library.contracts.ui.UICategory;
import com.roni.library.contracts.ui.UIComponentType;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Universal Configuration for NativeRuntimeUI supporting registry-driven components,
 * category defaults, individual component overrides, and animation parameters.
 */
public class NativeRuntimeUIConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    private final boolean debugEnabled;
    private final String tag;
    private final int timeout;
    private final int animationDurationMs;
    private final boolean bottomSheetDismissible;
    private final boolean autoDismissToasts;

    private final boolean globalDefault;
    private final Map<UICategory, Boolean> categoryDefaults;
    private final Map<UIComponentType, Boolean> componentOverrides;
    private final Map<UIComponentType, Map<String, Object>> componentProperties;

    private NativeRuntimeUIConfig(Builder builder) {
        this.debugEnabled = builder.debugEnabled;
        this.tag = builder.tag != null ? builder.tag : "NativeRuntimeUI";
        this.timeout = builder.timeout > 0 ? builder.timeout : 30000;
        this.animationDurationMs = builder.animationDurationMs > 0 ? builder.animationDurationMs : 300;
        this.bottomSheetDismissible = builder.bottomSheetDismissible;
        this.autoDismissToasts = builder.autoDismissToasts;

        this.globalDefault = builder.globalDefault;
        this.categoryDefaults = Collections.unmodifiableMap(new HashMap<>(builder.categoryDefaults));
        this.componentOverrides = Collections.unmodifiableMap(new HashMap<>(builder.componentOverrides));
        this.componentProperties = Collections.unmodifiableMap(new HashMap<>(builder.componentProperties));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static NativeRuntimeUIConfig defaultConfiguration() {
        return builder().build();
    }

    public boolean isDebugEnabled() { return debugEnabled; }
    public String getTag() { return tag; }
    public int getTimeout() { return timeout; }
    public int getAnimationDurationMs() { return animationDurationMs; }
    public boolean isBottomSheetDismissible() { return bottomSheetDismissible; }
    public boolean isAutoDismissToasts() { return autoDismissToasts; }

    public boolean getGlobalDefault() { return globalDefault; }
    @NonNull public Map<UICategory, Boolean> getCategoryDefaults() { return categoryDefaults; }
    @NonNull public Map<UIComponentType, Boolean> getComponentOverrides() { return componentOverrides; }
    @NonNull public Map<UIComponentType, Map<String, Object>> getComponentProperties() { return componentProperties; }

    /**
     * Resolves component enablement using strict priority:
     * Individual Component Override > Category Default > Global Default.
     */
    public boolean isComponentEnabled(@NonNull UIComponentType type) {
        Boolean override = componentOverrides.get(type);
        if (override != null) {
            return override;
        }

        Boolean catDefault = categoryDefaults.get(type.getCategory());
        if (catDefault != null) {
            return catDefault;
        }

        return globalDefault;
    }

    public static class Builder {
        private boolean debugEnabled = false;
        private String tag = "NativeRuntimeUI";
        private int timeout = 30000;
        private int animationDurationMs = 300;
        private boolean bottomSheetDismissible = true;
        private boolean autoDismissToasts = true;

        private boolean globalDefault = false;
        private final Map<UICategory, Boolean> categoryDefaults = new HashMap<>();
        private final Map<UIComponentType, Boolean> componentOverrides = new HashMap<>();
        private final Map<UIComponentType, Map<String, Object>> componentProperties = new HashMap<>();

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

        public Builder animationDurationMs(int animationDurationMs) {
            this.animationDurationMs = animationDurationMs;
            return this;
        }

        public Builder bottomSheetDismissible(boolean bottomSheetDismissible) {
            this.bottomSheetDismissible = bottomSheetDismissible;
            return this;
        }

        public Builder autoDismissToasts(boolean autoDismissToasts) {
            this.autoDismissToasts = autoDismissToasts;
            return this;
        }

        public Builder setGlobalDefault(boolean enabled) {
            this.globalDefault = enabled;
            return this;
        }

        public Builder enable(@NonNull UIComponentType type) {
            this.componentOverrides.put(type, true);
            return this;
        }

        public Builder disable(@NonNull UIComponentType type) {
            this.componentOverrides.put(type, false);
            return this;
        }

        public Builder enableCategory(@NonNull UICategory category) {
            this.categoryDefaults.put(category, true);
            return this;
        }

        public Builder disableCategory(@NonNull UICategory category) {
            this.categoryDefaults.put(category, false);
            return this;
        }

        public Builder configureComponent(@NonNull UIComponentType type, @NonNull String key, @Nullable Object value) {
            this.componentProperties.computeIfAbsent(type, k -> new HashMap<>()).put(key, value);
            return this;
        }

        public NativeRuntimeUIConfig build() {
            return new NativeRuntimeUIConfig(this);
        }
    }
}
