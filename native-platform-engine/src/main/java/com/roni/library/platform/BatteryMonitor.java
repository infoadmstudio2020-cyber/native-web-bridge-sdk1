package com.roni.library.platform;

import com.roni.library.platform.listener.BatteryListener;
import com.roni.library.platform.model.BatteryStatusDTO;

/**
 * Convenience monitor class for battery state and power telemetry.
 *
 * @author infoadmstudio2020-cyber
 */
public class BatteryMonitor {

    public static BatteryStatusDTO getBatteryStatus() {
        return PlatformEngine.getInstance().getBatteryService().getBatteryStatus();
    }

    public static void addListener(BatteryListener listener) {
        PlatformEngine.getInstance().getBatteryService().addListener(listener);
    }

    public static void removeListener(BatteryListener listener) {
        PlatformEngine.getInstance().getBatteryService().removeListener(listener);
    }
}
