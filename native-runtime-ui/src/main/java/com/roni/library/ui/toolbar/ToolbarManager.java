package com.roni.library.ui.toolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Manages action bar / toolbar title, navigation icon, and menu items.
 */
public class ToolbarManager {

    public static class ActionButton {
        private final String id;
        private final String label;
        private final Runnable action;

        public ActionButton(String id, String label, Runnable action) {
            this.id = id != null ? id : "";
            this.label = label != null ? label : "";
            this.action = action;
        }

        public String getId() {
            return id;
        }

        public String getLabel() {
            return label;
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
    private volatile String subtitle = "";
    private volatile boolean navigationIconVisible = true;
    private final List<ActionButton> actions = new CopyOnWriteArrayList<>();

    public ToolbarManager() {}

    public void setTitle(String title) {
        this.title = title != null ? title : "";
    }

    public String getTitle() {
        return title;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle != null ? subtitle : "";
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setNavigationIconVisible(boolean visible) {
        this.navigationIconVisible = visible;
    }

    public boolean isNavigationIconVisible() {
        return navigationIconVisible;
    }

    public void addAction(String id, String label, Runnable action) {
        actions.add(new ActionButton(id, label, action));
    }

    public int getActionCount() {
        return actions.size();
    }

    public List<ActionButton> getActions() {
        return new ArrayList<>(actions);
    }

    public void clearActions() {
        actions.clear();
    }
}
