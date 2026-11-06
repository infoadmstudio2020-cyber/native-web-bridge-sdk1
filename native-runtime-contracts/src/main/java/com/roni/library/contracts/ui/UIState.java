package com.roni.library.contracts.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Encapsulates reactive UI state for a component.
 */
public final class UIState implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String stateKey;
    private final Map<String, Object> data = new HashMap<>();

    public UIState(@NonNull String stateKey) {
        this.stateKey = stateKey;
    }

    @NonNull
    public String getStateKey() {
        return stateKey;
    }

    public synchronized void set(@NonNull String key, @Nullable Object value) {
        data.put(key, value);
    }

    @Nullable
    public synchronized Object get(@NonNull String key) {
        return data.get(key);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public synchronized <T> T get(@NonNull String key, @Nullable T defaultValue) {
        Object val = data.get(key);
        if (val == null) return defaultValue;
        try {
            return (T) val;
        } catch (ClassCastException e) {
            return defaultValue;
        }
    }

    public synchronized boolean contains(@NonNull String key) {
        return data.containsKey(key);
    }

    @NonNull
    public synchronized Map<String, Object> snapshot() {
        return Collections.unmodifiableMap(new HashMap<>(data));
    }
}
