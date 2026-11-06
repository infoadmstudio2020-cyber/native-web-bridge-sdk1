package com.roni.library.ui.state;

import android.os.Bundle;
import androidx.annotation.NonNull;
import com.roni.library.contracts.ui.IUIStateManager;
import com.roni.library.contracts.ui.UIComponentType;
import com.roni.library.contracts.ui.UIState;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UIStateManager implements IUIStateManager {

    private final Map<UIComponentType, UIState> componentStates = new ConcurrentHashMap<>();

    public UIStateManager() {}

    @NonNull
    @Override
    public UIState getComponentState(@NonNull UIComponentType type) {
        return componentStates.computeIfAbsent(type, t -> new UIState(t.getQualifiedId()));
    }

    @Override
    public void saveAllStates(@NonNull Bundle outState) {
        for (Map.Entry<UIComponentType, UIState> entry : componentStates.entrySet()) {
            UIState state = entry.getValue();
            Bundle compBundle = new Bundle();
            for (Map.Entry<String, Object> dataEntry : state.snapshot().entrySet()) {
                Object val = dataEntry.getValue();
                if (val instanceof String) compBundle.putString(dataEntry.getKey(), (String) val);
                else if (val instanceof Integer) compBundle.putInt(dataEntry.getKey(), (Integer) val);
                else if (val instanceof Boolean) compBundle.putBoolean(dataEntry.getKey(), (Boolean) val);
                else if (val instanceof Long) compBundle.putLong(dataEntry.getKey(), (Long) val);
                else if (val != null) compBundle.putString(dataEntry.getKey(), val.toString());
            }
            outState.putBundle("nr_ui_state_" + entry.getKey().getQualifiedId(), compBundle);
        }
    }

    @Override
    public void restoreAllStates(@NonNull Bundle inState) {
        for (String key : inState.keySet()) {
            if (key != null && key.startsWith("nr_ui_state_")) {
                Bundle compBundle = inState.getBundle(key);
                if (compBundle != null) {
                    String qualId = key.substring("nr_ui_state_".length());
                    for (UIComponentType type : componentStates.keySet()) {
                        if (type.getQualifiedId().equals(qualId)) {
                            UIState state = getComponentState(type);
                            for (String dataKey : compBundle.keySet()) {
                                state.set(dataKey, compBundle.get(dataKey));
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void reset() {
        componentStates.clear();
    }
}
