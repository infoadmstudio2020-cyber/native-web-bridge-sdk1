package com.roni.library.bridge.message;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Standard envelope representation for all bridge communications.
 */
public final class BridgeMessage {

    private final String id;
    private final BridgeMessageType type;
    private final String action;
    private final Map<String, Object> payload;
    private final String error;
    private final boolean success;
    private final long timestamp;

    public BridgeMessage(String id, BridgeMessageType type, String action,
                         Map<String, Object> payload, String error, boolean success, long timestamp) {
        this.id = id != null ? id : UUID.randomUUID().toString();
        this.type = type != null ? type : BridgeMessageType.REQUEST;
        this.action = action != null ? action : "";
        this.payload = payload != null ? Collections.unmodifiableMap(new HashMap<>(payload)) : Collections.emptyMap();
        this.error = error;
        this.success = success;
        this.timestamp = timestamp > 0 ? timestamp : System.currentTimeMillis();
    }

    public static BridgeMessage request(String id, String action, Map<String, Object> payload) {
        return new BridgeMessage(id, BridgeMessageType.REQUEST, action, payload, null, true, System.currentTimeMillis());
    }

    public static BridgeMessage response(String id, String action, Map<String, Object> payload) {
        return new BridgeMessage(id, BridgeMessageType.RESPONSE, action, payload, null, true, System.currentTimeMillis());
    }

    public static BridgeMessage error(String id, String action, String error) {
        return new BridgeMessage(id, BridgeMessageType.ERROR, action, null, error, false, System.currentTimeMillis());
    }

    public static BridgeMessage event(String topic, Map<String, Object> payload) {
        return new BridgeMessage(UUID.randomUUID().toString(), BridgeMessageType.EVENT, topic, payload, null, true, System.currentTimeMillis());
    }

    public static BridgeMessage ping(String id) {
        Map<String, Object> pong = new HashMap<>();
        pong.put("pong", true);
        pong.put("serverTime", System.currentTimeMillis());
        return new BridgeMessage(id, BridgeMessageType.PING, "ping", pong, null, true, System.currentTimeMillis());
    }

    public String getId() {
        return id;
    }

    public BridgeMessageType getType() {
        return type;
    }

    public String getAction() {
        return action;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public String getError() {
        return error;
    }

    public boolean isSuccess() {
        return success;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "BridgeMessage{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", action='" + action + '\'' +
                ", payload=" + payload +
                ", error='" + error + '\'' +
                ", success=" + success +
                ", timestamp=" + timestamp +
                '}';
    }
}
