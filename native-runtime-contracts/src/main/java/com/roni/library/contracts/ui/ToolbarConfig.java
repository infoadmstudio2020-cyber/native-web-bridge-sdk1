package com.roni.library.contracts.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Standard configuration object for the native host activity toolbar.
 *
 * @author infoadmstudio2020-cyber
 */
public final class ToolbarConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Single action button within the toolbar.
     */
    public static final class ActionItem implements Serializable {
        private static final long serialVersionUID = 1L;

        private final String id;
        private final String title;
        private final String iconRes;
        private final boolean visible;

        public ActionItem(@NonNull String id, @NonNull String title, @Nullable String iconRes, boolean visible) {
            this.id = id != null ? id : "";
            this.title = title != null ? title : "";
            this.iconRes = iconRes != null ? iconRes : "";
            this.visible = visible;
        }

        @NonNull
        public String getId() { return id; }

        @NonNull
        public String getTitle() { return title; }

        @NonNull
        public String getIconRes() { return iconRes; }

        public boolean isVisible() { return visible; }

        @NonNull
        @Override
        public String toString() {
            return "ActionItem{id='" + id + "', title='" + title + "', visible=" + visible + "}";
        }
    }

    private final String title;
    private final String subtitle;
    private final String backgroundColor;
    private final String titleColor;
    private final List<ActionItem> actions;

    public ToolbarConfig(@Nullable String title,
                         @Nullable String subtitle,
                         @Nullable String backgroundColor,
                         @Nullable String titleColor,
                         @Nullable List<ActionItem> actions) {
        this.title = title != null ? title : "";
        this.subtitle = subtitle != null ? subtitle : "";
        this.backgroundColor = backgroundColor != null ? backgroundColor : "#FFFFFF";
        this.titleColor = titleColor != null ? titleColor : "#000000";
        this.actions = actions != null ? Collections.unmodifiableList(new ArrayList<>(actions)) : Collections.<ActionItem>emptyList();
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public String getSubtitle() {
        return subtitle;
    }

    @NonNull
    public String getBackgroundColor() {
        return backgroundColor;
    }

    @NonNull
    public String getTitleColor() {
        return titleColor;
    }

    @NonNull
    public List<ActionItem> getActions() {
        return actions;
    }

    @NonNull
    @Override
    public String toString() {
        return "ToolbarConfig{title='" + title + "', subtitle='" + subtitle + "', actions=" + actions.size() + "}";
    }

    public static final class Builder {
        private String title = "";
        private String subtitle = "";
        private String backgroundColor = "#FFFFFF";
        private String titleColor = "#000000";
        private final List<ActionItem> actions = new ArrayList<>();

        @NonNull
        public Builder title(@Nullable String title) {
            this.title = title;
            return this;
        }

        @NonNull
        public Builder subtitle(@Nullable String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        @NonNull
        public Builder backgroundColor(@Nullable String colorHex) {
            this.backgroundColor = colorHex;
            return this;
        }

        @NonNull
        public Builder titleColor(@Nullable String colorHex) {
            this.titleColor = colorHex;
            return this;
        }

        @NonNull
        public Builder addAction(@NonNull ActionItem item) {
            this.actions.add(item);
            return this;
        }

        @NonNull
        public ToolbarConfig build() {
            return new ToolbarConfig(title, subtitle, backgroundColor, titleColor, actions);
        }
    }
}
