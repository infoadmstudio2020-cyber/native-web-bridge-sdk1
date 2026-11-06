package com.roni.library.ui.overlay;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Coordinates global screen-blocking loading spinners and backdrop overlays.
 */
public class LoadingOverlayManager {

    private final AtomicBoolean showing = new AtomicBoolean(false);
    private volatile String text = "Loading...";

    public LoadingOverlayManager() {}

    public void show(String text) {
        this.text = text != null ? text : "Loading...";
        this.showing.set(true);
    }

    public void hide() {
        this.showing.set(false);
    }

    public boolean isShowing() {
        return showing.get();
    }

    public String getText() {
        return text;
    }
}
