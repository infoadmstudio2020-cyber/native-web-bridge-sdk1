package com.roni.library.bridge.serializer;

import com.roni.library.bridge.message.BridgeMessage;
import com.roni.library.bridge.message.BridgeMessageType;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Production-ready JSON serializer using Android / standard org.json.
 */
public final class JsonMessageSerializer implements IMessageSerializer {

    @Override
    public String serialize(BridgeMessage message) {
        if (message == null) {
            return "{}";
        }
        try {
            JSONObject obj = new JSONObject();
            obj.put("id", message.getId());
            obj.put("type", message.getType() != null ? message.getType().name() : "REQUEST");
            obj.put("action", message.getAction());
            obj.put("success", message.isSuccess());
            obj.put("timestamp", message.getTimestamp());
            if (message.getError() != null) {
                obj.put("error", message.getError());
            }
            if (message.getPayload() != null && !message.getPayload().isEmpty()) {
                obj.put("payload", mapToJsonObject(message.getPayload()));
            } else {
                obj.put("payload", new JSONObject());
            }
            return obj.toString();
        } catch (Exception e) {
            return "{\"id\":\"" + escape(message.getId()) + "\",\"error\":\"Serialization failed: " + escape(e.getMessage()) + "\",\"success\":false}";
        }
    }

    @Override
    public String serializeMap(Map<String, ?> map) {
        if (map == null) {
            return "{}";
        }
        try {
            return mapToJsonObject(map).toString();
        } catch (Exception e) {
            return "{}";
        }
    }

    @Override
    public BridgeMessage deserialize(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            return BridgeMessage.error("unknown", "", "Empty raw payload");
        }
        try {
            JSONObject obj = new JSONObject(raw.trim());
            String id = obj.optString("id", "req_" + System.currentTimeMillis());
            String typeStr = obj.optString("type", "REQUEST");
            BridgeMessageType type = BridgeMessageType.fromString(typeStr);
            String action = obj.optString("action", "");
            String error = obj.has("error") && !obj.isNull("error") ? obj.getString("error") : null;
            boolean success = obj.optBoolean("success", error == null);
            long timestamp = obj.optLong("timestamp", System.currentTimeMillis());

            Map<String, Object> payload = Collections.emptyMap();
            if (obj.has("payload") && !obj.isNull("payload")) {
                Object p = obj.get("payload");
                if (p instanceof JSONObject) {
                    payload = jsonObjectToMap((JSONObject) p);
                }
            }

            return new BridgeMessage(id, type, action, payload, error, success, timestamp);
        } catch (Exception e) {
            return BridgeMessage.error("parse_error", "", "Failed to parse JSON: " + e.getMessage());
        }
    }

    public static JSONObject mapToJsonObject(Map<String, ?> map) {
        JSONObject json = new JSONObject();
        if (map == null) {
            return json;
        }
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            try {
                json.put(entry.getKey(), wrap(entry.getValue()));
            } catch (Exception ignored) {
            }
        }
        return json;
    }

    public static Map<String, Object> jsonObjectToMap(JSONObject json) {
        Map<String, Object> map = new HashMap<>();
        if (json == null) {
            return map;
        }
        Iterator<String> keys = json.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object value = json.opt(key);
            map.put(key, unwrap(value));
        }
        return map;
    }

    private static Object wrap(Object o) {
        if (o == null) {
            return JSONObject.NULL;
        }
        if (o instanceof Map) {
            return mapToJsonObject((Map<String, ?>) o);
        }
        if (o instanceof Iterable) {
            JSONArray arr = new JSONArray();
            for (Object item : (Iterable<?>) o) {
                arr.put(wrap(item));
            }
            return arr;
        }
        return o;
    }

    private static Object unwrap(Object o) {
        if (o == null || o == JSONObject.NULL) {
            return null;
        }
        if (o instanceof JSONObject) {
            return jsonObjectToMap((JSONObject) o);
        }
        if (o instanceof JSONArray) {
            JSONArray arr = (JSONArray) o;
            List<Object> list = new ArrayList<>(arr.length());
            for (int i = 0; i < arr.length(); i++) {
                list.add(unwrap(arr.opt(i)));
            }
            return list;
        }
        return o;
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("\"", "\\\"");
    }
}
