package com.roni.library.contracts.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Strongly typed identifier for a UI sub-component within a UICategory.
 */
public final class UIComponentType implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UICategory category;
    private final String key;
    private final String displayName;

    public UIComponentType(@NonNull UICategory category, @NonNull String key, @NonNull String displayName) {
        this.category = Objects.requireNonNull(category, "category cannot be null");
        this.key = Objects.requireNonNull(key, "key cannot be null");
        this.displayName = displayName != null ? displayName : key;
    }

    public UIComponentType(@NonNull UICategory category, @NonNull String key) {
        this(category, key, key);
    }

    @NonNull
    public UICategory getCategory() {
        return category;
    }

    @NonNull
    public String getKey() {
        return key;
    }

    @NonNull
    public String getDisplayName() {
        return displayName;
    }

    @NonNull
    public String getQualifiedId() {
        return category.name() + ":" + key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UIComponentType that = (UIComponentType) o;
        return category == that.category && key.equals(that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, key);
    }

    @NonNull
    @Override
    public String toString() {
        return getQualifiedId();
    }
}
