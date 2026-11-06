package com.roni.library.ui;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Controller and state representation for draggable, smooth bottom sheet dialogs.
 */
public class CustomBottomSheet {

    private final AtomicBoolean visible = new AtomicBoolean(false);
    private final AtomicBoolean expanded = new AtomicBoolean(false);
    private volatile String title = "";
    private volatile boolean dismissible = true;

    public CustomBottomSheet() {}

    public void show() {
        visible.set(true);
    }

    public void dismiss() {
        visible.set(false);
        expanded.set(false);
    }

    public boolean isVisible() {
        return visible.get();
    }

    public void setTitle(String title) {
        this.title = title != null ? title : "";
    }

    public String getTitle() {
        return title;
    }

    public void setExpanded(boolean expanded) {
        this.expanded.set(expanded);
    }

    public boolean isExpanded() {
        return expanded.get();
    }

    public void setDismissible(boolean dismissible) {
        this.dismissible = dismissible;
    }

    public boolean isDismissible() {
        return dismissible;
    }
}
