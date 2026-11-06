package com.roni.library.contracts.common;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Execution context carrying correlation and session telemetry across layer boundaries.
 *
 * @author infoadmstudio2020-cyber
 */
public final class ExecutionContext implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String sessionId;
    private final String correlationId;
    private final long timestamp;
    private final Map<String, Object> attributes;

    public ExecutionContext(@Nullable String sessionId, @Nullable String correlationId, @Nullable Map<String, Object> attributes) {
        this.sessionId = sessionId != null ? sessionId : UUID.randomUUID().toString();
        this.correlationId = correlationId != null ? correlationId : UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis();
        this.attributes = attributes != null ? Collections.unmodifiableMap(new HashMap<>(attributes)) : Collections.emptyMap();
    }

    public ExecutionContext() {
        this(null, null, null);
    }

    @NonNull
    public String getSessionId() {
        return sessionId;
    }

    @NonNull
    public String getCorrelationId() {
        return correlationId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @NonNull
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Nullable
    public Object getAttribute(@NonNull String key) {
        return attributes.get(key);
    }

    @NonNull
    @Override
    public String toString() {
        return "ExecutionContext{sessionId='" + sessionId + "', correlationId='" + correlationId + "', timestamp=" + timestamp + "}";
    }
}
