package com.roni.library.bridge;

import com.roni.library.bridge.message.BridgeMessage;

/**
 * Validates message integrity, origin whitelist permissions, and payload structure.
 */
public final class BridgeSecurityValidator {

    private final BridgeConfig config;

    public BridgeSecurityValidator(BridgeConfig config) {
        this.config = config != null ? config : BridgeConfig.defaultConfig();
    }

    public boolean isOriginValid(String origin) {
        return config.isOriginAllowed(origin);
    }

    public boolean isMessageValid(BridgeMessage message) {
        if (message == null) {
            return false;
        }
        if (message.getId() == null || message.getId().trim().isEmpty()) {
            return false;
        }
        if (message.getAction() == null || message.getAction().trim().isEmpty()) {
            return false;
        }
        return true;
    }
}
