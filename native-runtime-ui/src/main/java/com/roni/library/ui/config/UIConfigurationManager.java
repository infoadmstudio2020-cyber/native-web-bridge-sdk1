package com.roni.library.ui.config;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.roni.library.contracts.ui.IUIConfigurationManager;
import com.roni.library.contracts.ui.UICategory;
import com.roni.library.contracts.ui.UIComponentType;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implements configuration priority:
 * Individual Component Override > Category Default > Global Default.
 */
public class UIConfigurationManager implements IUIConfigurationManager {

    private volatile boolean globalDefault = false;
    private final Map<UICategory, Boolean> categoryDefaults = new ConcurrentHashMap<>();
    private final Map<UIComponentType, Boolean> componentOverrides = new ConcurrentHashMap<>();
    private final Map<UIComponentType, Map<String, Object>> componentProperties = new ConcurrentHashMap<>();

    public UIConfigurationManager() {}

    @Override
    public void setGlobalDefault(boolean enabled) {
        this.globalDefault = enabled;
    }

    @Override
    public boolean getGlobalDefault() {
        return globalDefault;
    }

    @Override
    public void setCategoryDefault(@NonNull UICategory category, boolean enabled) {
        categoryDefaults.put(category, enabled);
    }

    @Nullable
    @Override
    public Boolean getCategoryDefault(@NonNull UICategory category) {
        return categoryDefaults.get(category);
    }

    @Override
    public void setComponentOverride(@NonNull UIComponentType type, boolean enabled) {
        componentOverrides.put(type, enabled);
    }

    @Nullable
    @Override
    public Boolean getComponentOverride(@NonNull UIComponentType type) {
        return componentOverrides.get(type);
    }

    @Override
    public boolean isComponentEnabled(@NonNull UIComponentType type) {
        // Priority 1: Individual Component Override
        Boolean override = componentOverrides.get(type);
        if (override != null) {
            return override;
        }

        // Priority 2: Category Default
        Boolean catDefault = categoryDefaults.get(type.getCategory());
        if (catDefault != null) {
            return catDefault;
        }

        // Priority 3: Global Default
        return globalDefault;
    }

    @Override
    public void setComponentProperty(@NonNull UIComponentType type, @NonNull String key, @Nullable Object value) {
        componentProperties.computeIfAbsent(type, k -> new ConcurrentHashMap<>()).put(key, value);
    }

    @Nullable
    @Override
    public Object getComponentProperty(@NonNull UIComponentType type, @NonNull String key, @Nullable Object defaultValue) {
        Map<String, Object> props = componentProperties.get(type);
        if (props == null || !props.containsKey(key)) {
            return defaultValue;
        }
        return props.get(key);
    }

    @NonNull
    @Override
    public Map<String, Object> getComponentProperties(@NonNull UIComponentType type) {
        Map<String, Object> props = componentProperties.get(type);
        if (props == null) {
            return Collections.emptyMap();
        }
        return Collections.unmodifiableMap(new HashMap<>(props));
    }

    public void clear() {
        globalDefault = false;
        categoryDefaults.clear();
        componentOverrides.clear();
        componentProperties.clear();
    }
}
