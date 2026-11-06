package com.roni.library.ui.registry;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.roni.library.contracts.ui.IUIComponent;
import com.roni.library.contracts.ui.IUIComponentDescriptor;
import com.roni.library.contracts.ui.IUIComponentProvider;
import com.roni.library.contracts.ui.IUIComponentRegistry;
import com.roni.library.contracts.ui.UICategory;
import com.roni.library.contracts.ui.UIComponentType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UIComponentRegistry implements IUIComponentRegistry {

    private final Map<UIComponentType, IUIComponentDescriptor> descriptors = new ConcurrentHashMap<>();
    private final Map<UIComponentType, IUIComponentProvider> providers = new ConcurrentHashMap<>();
    private final Map<UIComponentType, IUIComponent> activeComponents = new ConcurrentHashMap<>();

    public UIComponentRegistry() {}

    @Override
    public void registerDescriptor(@NonNull IUIComponentDescriptor descriptor) {
        descriptors.put(descriptor.getType(), descriptor);
    }

    @Override
    public void registerProvider(@NonNull IUIComponentProvider provider) {
        providers.put(provider.getProvidedType(), provider);
    }

    @Nullable
    @Override
    public IUIComponentDescriptor getDescriptor(@NonNull UIComponentType type) {
        return descriptors.get(type);
    }

    @NonNull
    @Override
    public List<IUIComponentDescriptor> getDescriptorsByCategory(@NonNull UICategory category) {
        List<IUIComponentDescriptor> list = new ArrayList<>();
        for (IUIComponentDescriptor desc : descriptors.values()) {
            if (desc.getCategory() == category) {
                list.add(desc);
            }
        }
        return Collections.unmodifiableList(list);
    }

    @NonNull
    @Override
    public List<IUIComponentDescriptor> getAllDescriptors() {
        return Collections.unmodifiableList(new ArrayList<>(descriptors.values()));
    }

    @Nullable
    @Override
    public IUIComponent getComponent(@NonNull UIComponentType type) {
        return activeComponents.get(type);
    }

    @NonNull
    @Override
    public synchronized IUIComponent getOrCreateComponent(@NonNull Context context, @NonNull UIComponentType type) {
        IUIComponent existing = activeComponents.get(type);
        if (existing != null) {
            return existing;
        }

        IUIComponentDescriptor desc = descriptors.get(type);
        if (desc == null) {
            desc = new UIComponentDescriptor(type, type.getDisplayName(), "Dynamic component", false, 0);
            descriptors.put(type, desc);
        }

        IUIComponentProvider provider = providers.get(type);
        if (provider == null) {
            throw new IllegalStateException("No UIComponentProvider registered for component: " + type);
        }

        IUIComponent created = provider.createComponent(context, desc);
        activeComponents.put(type, created);
        return created;
    }

    @Override
    public boolean isRegistered(@NonNull UIComponentType type) {
        return descriptors.containsKey(type);
    }

    @Override
    public int getComponentCount() {
        return descriptors.size();
    }

    public synchronized void clear() {
        for (IUIComponent comp : activeComponents.values()) {
            try {
                comp.destroy();
            } catch (Throwable ignored) {}
        }
        activeComponents.clear();
        descriptors.clear();
        providers.clear();
    }
}
