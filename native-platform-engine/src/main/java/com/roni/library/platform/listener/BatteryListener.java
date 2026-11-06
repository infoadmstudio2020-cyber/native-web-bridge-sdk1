package com.roni.library.platform.listener;

import com.roni.library.platform.model.BatteryStatusDTO;

/**
 * Listener interface for battery state change notifications.
 *
 * @author infoadmstudio2020-cyber
 */
public interface BatteryListener {

    /**
     * Called when battery level, charging state, or thermal metrics change.
     *
     * @param status Updated battery status
     */
    void onBatteryChanged(BatteryStatusDTO status);
}
