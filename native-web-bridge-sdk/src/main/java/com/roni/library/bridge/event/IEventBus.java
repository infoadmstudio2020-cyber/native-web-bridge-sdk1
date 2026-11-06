package com.roni.library.bridge.event;

/**
 * Contract for subscribing to and publishing bridge events.
 */
public interface IEventBus {
    void subscribe(String topic, BridgeEventListener listener);
    void unsubscribe(String topic, BridgeEventListener listener);
    void publish(BridgeEvent event);
    void clear();
}
