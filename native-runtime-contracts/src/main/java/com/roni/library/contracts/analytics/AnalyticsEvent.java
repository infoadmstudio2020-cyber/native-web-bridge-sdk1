package com.roni.library.contracts.analytics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Universal analytics event data model for the NativeRuntime ecosystem.
 *
 * @author infoadmstudio2020-cyber
 */
public final class AnalyticsEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;
    private final Map<String, Object> parameters;
    private final long timestamp;

    public AnalyticsEvent(@NonNull String name, @Nullable Map<String, Object> parameters, long timestamp) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Event name cannot be null or empty");
        }
        this.name = name;
        this.parameters = parameters != null ? Collections.unmodifiableMap(new HashMap<>(parameters)) : Collections.emptyMap();
        this.timestamp = timestamp > 0 ? timestamp : System.currentTimeMillis();
    }

    public AnalyticsEvent(@NonNull String name, @Nullable Map<String, Object> parameters) {
        this(name, parameters, System.currentTimeMillis());
    }

    public AnalyticsEvent(@NonNull String name) {
        this(name, null, System.currentTimeMillis());
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public Map<String, Object> getParameters() {
        return parameters;
    }

    @Nullable
    public Object getParameter(@NonNull String key) {
        return parameters.get(key);
    }

    public long getTimestamp() {
        return timestamp;
    }

    @NonNull
    @Override
    public String toString() {
        return "AnalyticsEvent{name='" + name + "', parameters=" + parameters + ", timestamp=" + timestamp + "}";
    }
}
