package com.roni.library.bridge.message;

/**
 * Types of messages transported across the native-web bridge.
 */
public enum BridgeMessageType {
    REQUEST,
    RESPONSE,
    ERROR,
    EVENT,
    PING;

    public static BridgeMessageType fromString(String val) {
        if (val == null) {
            return REQUEST;
        }
        try {
            return BridgeMessageType.valueOf(val.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            return REQUEST;
        }
    }
}
