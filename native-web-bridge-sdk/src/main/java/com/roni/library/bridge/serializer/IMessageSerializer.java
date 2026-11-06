package com.roni.library.bridge.serializer;

import com.roni.library.bridge.message.BridgeMessage;
import java.util.Map;

/**
 * Serialization contract for bridge messages and data payloads.
 */
public interface IMessageSerializer {
    String serialize(BridgeMessage message);
    String serializeMap(Map<String, ?> map);
    BridgeMessage deserialize(String raw);
}
