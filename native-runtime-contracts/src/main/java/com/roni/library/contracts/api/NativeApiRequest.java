package com.roni.library.contracts.api;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Universal RPC request payload for NativeRuntime API communication.
 *
 * @author infoadmstudio2020-cyber
 */
public final class NativeApiRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String requestId;
    private final String path;
    private final String action;
    private final Map<String, Object> parameters;
    private final long timestamp;

    public NativeApiRequest(@NonNull String requestId,
                            @NonNull String path,
                            @NonNull String action,
                            @Nullable Map<String, Object> parameters,
                            long timestamp) {
        this.requestId = requestId != null ? requestId : UUID.randomUUID().toString();
        this.path = path != null ? path : "";
        this.action = action != null ? action : "";
        this.parameters = parameters != null ? Collections.unmodifiableMap(new HashMap<>(parameters)) : Collections.emptyMap();
        this.timestamp = timestamp > 0 ? timestamp : System.currentTimeMillis();
    }

    @NonNull
    public String getRequestId() {
        return requestId;
    }

    @NonNull
    public String getPath() {
        return path;
    }

    @NonNull
    public String getAction() {
        return action;
    }

    @NonNull
    public Map<String, Object> getParameters() {
        return parameters;
    }

    @Nullable
    public Object getParameter(@NonNull String key) {
        return parameters.get(key);
    }

    @NonNull
    public String getStringParam(@NonNull String key, @NonNull String defaultValue) {
        Object val = parameters.get(key);
        return val != null ? String.valueOf(val) : defaultValue;
    }

    public boolean getBooleanParam(@NonNull String key, boolean defaultValue) {
        Object val = parameters.get(key);
        if (val instanceof Boolean) {
            return (Boolean) val;
        } else if (val instanceof String) {
            return Boolean.parseBoolean((String) val);
        }
        return defaultValue;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @NonNull
    @Override
    public String toString() {
        return "NativeApiRequest{requestId='" + requestId + "', path='" + path + "', action='" + action + "', parameters=" + parameters + "}";
    }

    /**
     * Builder for constructing {@link NativeApiRequest}.
     */
    public static final class Builder {
        private String requestId;
        private String path = "";
        private String action = "";
        private final Map<String, Object> parameters = new HashMap<>();
        private long timestamp = System.currentTimeMillis();

        public Builder() {
            this.requestId = UUID.randomUUID().toString();
        }

        public Builder(@NonNull String path, @NonNull String action) {
            this.requestId = UUID.randomUUID().toString();
            this.path = path;
            this.action = action;
        }

        @NonNull
        public Builder requestId(@NonNull String requestId) {
            this.requestId = requestId;
            return this;
        }

        @NonNull
        public Builder path(@NonNull String path) {
            this.path = path;
            return this;
        }

        @NonNull
        public Builder action(@NonNull String action) {
            this.action = action;
            return this;
        }

        @NonNull
        public Builder param(@NonNull String key, @Nullable Object value) {
            this.parameters.put(key, value);
            return this;
        }

        @NonNull
        public Builder parameters(@Nullable Map<String, Object> params) {
            if (params != null) {
                this.parameters.putAll(params);
            }
            return this;
        }

        @NonNull
        public Builder timestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        @NonNull
        public NativeApiRequest build() {
            return new NativeApiRequest(requestId, path, action, parameters, timestamp);
        }
    }
}
