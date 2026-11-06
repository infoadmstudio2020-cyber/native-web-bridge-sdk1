package com.roni.library.bridge.event;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Thread-safe implementation of IEventBus.
 */
public final class BridgeEventBus implements IEventBus {

    private final ConcurrentHashMap<String, CopyOnWriteArrayList<BridgeEventListener>> listeners =
            new ConcurrentHashMap<>();

    @Override
    public void subscribe(String topic, BridgeEventListener listener) {
        if (topic == null || listener == null) {
            return;
        }
        listeners.computeIfAbsent(topic, k -> new CopyOnWriteArrayList<>()).add(listener);
    }

    @Override
    public void unsubscribe(String topic, BridgeEventListener listener) {
        if (topic == null || listener == null) {
            return;
        }
        CopyOnWriteArrayList<BridgeEventListener> list = listeners.get(topic);
        if (list != null) {
            list.remove(listener);
            if (list.isEmpty()) {
                listeners.remove(topic);
            }
        }
    }

    @Override
    public void publish(BridgeEvent event) {
        if (event == null || event.getTopic() == null) {
            return;
        }
        CopyOnWriteArrayList<BridgeEventListener> topicListeners = listeners.get(event.getTopic());
        if (topicListeners != null) {
            for (BridgeEventListener listener : topicListeners) {
                try {
                    listener.onEvent(event);
                } catch (Throwable ignored) {
                }
            }
        }

        // Global wildcard listeners ("*")
        CopyOnWriteArrayList<BridgeEventListener> wildcardListeners = listeners.get("*");
        if (wildcardListeners != null) {
            for (BridgeEventListener listener : wildcardListeners) {
                try {
                    listener.onEvent(event);
                } catch (Throwable ignored) {
                }
            }
        }
    }

    @Override
    public void clear() {
        listeners.clear();
    }
}
