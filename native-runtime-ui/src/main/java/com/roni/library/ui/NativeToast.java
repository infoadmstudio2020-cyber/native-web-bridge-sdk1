package com.roni.library.ui;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Controller and state model for transient Toast alerts and snackbars.
 */
public class NativeToast {

    public enum Duration {
        SHORT, LONG
    }

    public enum Type {
        INFO, SUCCESS, WARNING, ERROR
    }

    private final AtomicBoolean visible = new AtomicBoolean(false);
    private volatile String message = "";
    private volatile Duration duration = Duration.SHORT;
    private volatile Type type = Type.INFO;

    public NativeToast() {}

    public void show(String message) {
        show(message, Duration.SHORT, Type.INFO);
    }

    public void show(String message, Duration duration, Type type) {
        this.message = message != null ? message : "";
        this.duration = duration != null ? duration : Duration.SHORT;
        this.type = type != null ? type : Type.INFO;
        this.visible.set(true);
    }

    public void dismiss() {
        this.visible.set(false);
    }

    public boolean isVisible() {
        return visible.get();
    }

    public String getMessage() {
        return message;
    }

    public Duration getDuration() {
        return duration;
    }

    public Type getType() {
        return type;
    }
}
