package com.roni.library.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Modal action sheet offering multiple action items, destructive options, and cancel callbacks.
 */
public class NativeActionSheet {

    public static class ActionItem {
        private final String title;
        private final boolean destructive;
        private final Runnable action;

        public ActionItem(String title, boolean destructive, Runnable action) {
            this.title = title != null ? title : "";
            this.destructive = destructive;
            this.action = action;
        }

        public String getTitle() {
            return title;
        }

        public boolean isDestructive() {
            return destructive;
        }

        public void execute() {
            if (action != null) {
                try {
                    action.run();
                } catch (Throwable ignored) {}
            }
        }
    }

    private volatile String title = "";
    private final List<ActionItem> actions = new CopyOnWriteArrayList<>();
    private final AtomicBoolean visible = new AtomicBoolean(false);

    public NativeActionSheet() {}

    public void setTitle(String title) {
        this.title = title != null ? title : "";
    }

    public String getTitle() {
        return title;
    }

    public void addAction(String title, boolean destructive, Runnable action) {
        actions.add(new ActionItem(title, destructive, action));
    }

    public void show() {
        visible.set(true);
    }

    public void dismiss() {
        visible.set(false);
    }

    public boolean isVisible() {
        return visible.get();
    }

    public int getActionCount() {
        return actions.size();
    }

    public List<ActionItem> getActions() {
        return new ArrayList<>(actions);
    }

    public void clear() {
        actions.clear();
        visible.set(false);
    }
}
