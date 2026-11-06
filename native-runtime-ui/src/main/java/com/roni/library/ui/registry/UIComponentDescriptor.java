package com.roni.library.ui.registry;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.roni.library.contracts.ui.IUIComponentDescriptor;
import com.roni.library.contracts.ui.UICategory;
import com.roni.library.contracts.ui.UIComponentType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class UIComponentDescriptor implements IUIComponentDescriptor {

    private final UIComponentType type;
    private final String name;
    private final String description;
    private final UICategory category;
    private final boolean enabledByDefault;
    private final int priority;
    private final List<UIComponentType> dependencies;

    public UIComponentDescriptor(@NonNull UIComponentType type,
                                 @NonNull String name,
                                 @NonNull String description,
                                 @NonNull UICategory category,
                                 boolean enabledByDefault,
                                 int priority,
                                 @Nullable List<UIComponentType> dependencies) {
        this.type = Objects.requireNonNull(type, "type cannot be null");
        this.name = Objects.requireNonNull(name, "name cannot be null");
        this.description = description != null ? description : "";
        this.category = Objects.requireNonNull(category, "category cannot be null");
        this.enabledByDefault = enabledByDefault;
        this.priority = priority;
        this.dependencies = dependencies != null ? Collections.unmodifiableList(new ArrayList<>(dependencies)) : Collections.<UIComponentType>emptyList();
    }

    public UIComponentDescriptor(@NonNull UIComponentType type,
                                 @NonNull String name,
                                 @NonNull String description,
                                 boolean enabledByDefault,
                                 int priority) {
        this(type, name, description, type.getCategory(), enabledByDefault, priority, null);
    }

    @NonNull
    @Override
    public UIComponentType getType() { return type; }

    @NonNull
    @Override
    public String getName() { return name; }

    @NonNull
    @Override
    public String getDescription() { return description; }

    @NonNull
    @Override
    public UICategory getCategory() { return category; }

    @Override
    public boolean isEnabledByDefault() { return enabledByDefault; }

    @Override
    public int getPriority() { return priority; }

    @NonNull
    @Override
    public List<UIComponentType> getDependencies() { return dependencies; }

    @NonNull
    @Override
    public String toString() {
        return "UIComponentDescriptor{" + type + " [priority=" + priority + ", default=" + enabledByDefault + "]}";
    }
}
