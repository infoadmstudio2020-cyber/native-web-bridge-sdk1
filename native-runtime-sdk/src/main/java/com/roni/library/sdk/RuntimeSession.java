package com.roni.library.sdk;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Tracks the live active runtime session state and duration metrics.
 */
public final class RuntimeSession implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum State {
        INITIALIZING,
        ACTIVE,
        PAUSED,
        STOPPED
    }

    private final String sessionId;
    private final long startTimeMs;
    private final AtomicLong lastActiveTimeMs;
    private volatile State state;
    private volatile long durationMs;

    public RuntimeSession() {
        this.sessionId = UUID.randomUUID().toString();
        this.startTimeMs = System.currentTimeMillis();
        this.lastActiveTimeMs = new AtomicLong(this.startTimeMs);
        this.state = State.INITIALIZING;
        this.durationMs = 0L;
    }

    public String getSessionId() {
        return sessionId;
    }

    public long getStartTimeMs() {
        return startTimeMs;
    }

    public long getLastActiveTimeMs() {
        return lastActiveTimeMs.get();
    }

    public State getState() {
        return state;
    }

    public void markActive() {
        this.state = State.ACTIVE;
        this.lastActiveTimeMs.set(System.currentTimeMillis());
        this.durationMs = this.lastActiveTimeMs.get() - startTimeMs;
    }

    public void markPaused() {
        this.state = State.PAUSED;
        this.durationMs = System.currentTimeMillis() - startTimeMs;
    }

    public void markStopped() {
        this.state = State.STOPPED;
        this.durationMs = System.currentTimeMillis() - startTimeMs;
    }

    public long getDurationMs() {
        if (state == State.ACTIVE) {
            return System.currentTimeMillis() - startTimeMs;
        }
        return durationMs;
    }

    @Override
    public String toString() {
        return "RuntimeSession{id='" + sessionId + "', state=" + state + ", duration=" + getDurationMs() + "ms}";
    }
}
