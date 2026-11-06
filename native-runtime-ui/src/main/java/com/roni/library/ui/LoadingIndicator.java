package com.roni.library.ui;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Controls hardware-accelerated loading spinners and progress overlays.
 */
public class LoadingIndicator {

    private final AtomicBoolean showing = new AtomicBoolean(false);
    private volatile String message = "Loading...";
    private volatile int progress = 0;

    public LoadingIndicator() {}

    public void show() {
        show("Loading...");
    }

    public void show(String message) {
        this.message = message != null ? message : "Loading...";
        this.showing.set(true);
    }

    public void hide() {
        this.showing.set(false);
        this.progress = 0;
    }

    public boolean isShowing() {
        return showing.get();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message != null ? message : "";
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = Math.max(0, Math.min(100, progress));
    }
}
