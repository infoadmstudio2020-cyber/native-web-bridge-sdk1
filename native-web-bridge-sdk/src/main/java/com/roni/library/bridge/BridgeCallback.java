package com.roni.library.bridge;

import com.roni.library.bridge.message.BridgeMessage;

/**
 * Callback for asynchronous bridge invocations.
 */
public interface BridgeCallback {
    void onSuccess(BridgeMessage message);
    void onError(String error);
}
