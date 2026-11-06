package com.roni.library.contracts.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Standard event dispatched by UI components across the ecosystem.
 */
public final class UIEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String eventType;
    private final UIComponentType sourceComponent;
    private final Map<String, Object> payload;
    private final long timestamp;

    public UIEvent(@NonNull String eventType,
                   @Nullable UIComponentType sourceComponent,
                   @Nullable Map<String, Object> payload) {
        this.eventType = eventType;
        this.sourceComponent = sourceComponent;
        this.payload = payload != null ? Collections.unmodifiableMap(new HashMap<>(payload)) : Collections.<String, Object>emptyMap();
        this.timestamp = System.currentTimeMillis();
    }

    public UIEvent(@NonNull String eventType, @Nullable UIComponentType sourceComponent) {
        this(eventType, sourceComponent, null);
    }

    @NonNull
    public String getEventType() {
        return eventType;
    }

    @Nullable
    public UIComponentType getSourceComponent() {
        return sourceComponent;
    }

    @NonNull
    public Map<String, Object> getPayload() {
        return payload;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @NonNull
    @Override
    public String toString() {
        return "UIEvent{type="" + eventType + "", source=" + sourceComponent + ", timestamp=" + timestamp + "}";
    }
}
