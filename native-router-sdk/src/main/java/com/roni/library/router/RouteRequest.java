package com.roni.library.router;

import com.roni.library.contracts.api.NativeApiRequest;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * High-level routing request model supporting fluent configuration.
 *
 * @author infoadmstudio2020-cyber
 */
public class RouteRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String uri;
    private final String action;
    private final Map<String, Object> parameters;
    private final String requestId;
    private final long timestamp;

    public RouteRequest(String uri, String action, Map<String, Object> parameters) {
        this.uri = uri;
        this.action = action != null ? action : "default";
        this.parameters = parameters != null ? new HashMap<>(parameters) : new HashMap<>();
        this.requestId = UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis();
    }

    public RouteRequest(String uri) {
        this(uri, "default", new HashMap<>());
    }

    public String getUri() {
        return uri;
    }

    public String getAction() {
        return action;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public String getRequestId() {
        return requestId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public RouteRequest withParameter(String key, Object value) {
        this.parameters.put(key, value);
        return this;
    }

    /**
     * Converts this RouteRequest into a standard Layer-0 {@link NativeApiRequest}.
     */
    public NativeApiRequest toNativeApiRequest() {
        return new NativeApiRequest(requestId, uri, action, parameters, timestamp);
    }
}
