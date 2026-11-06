package com.roni.library.contracts.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.Map;

public interface IUIConfigurationManager {
    void setGlobalDefault(boolean enabled);
    boolean getGlobalDefault();
    void setCategoryDefault(@NonNull UICategory category, boolean enabled);
    @Nullable
    Boolean getCategoryDefault(@NonNull UICategory category);
    void setComponentOverride(@NonNull UIComponentType type, boolean enabled);
    @Nullable
    Boolean getComponentOverride(@NonNull UIComponentType type);
    boolean isComponentEnabled(@NonNull UIComponentType type);
    void setComponentProperty(@NonNull UIComponentType type, @NonNull String key, @Nullable Object value);
    @Nullable
    Object getComponentProperty(@NonNull UIComponentType type, @NonNull String key, @Nullable Object defaultValue);
    @NonNull
    Map<String, Object> getComponentProperties(@NonNull UIComponentType type);
}
