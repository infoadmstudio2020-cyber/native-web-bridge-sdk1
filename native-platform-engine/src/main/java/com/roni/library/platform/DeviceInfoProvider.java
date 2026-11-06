package com.roni.library.platform;

import com.roni.library.platform.model.DeviceInfoDTO;

/**
 * Convenience provider class for device specifications and hardware fingerprinting.
 *
 * @author infoadmstudio2020-cyber
 */
public class DeviceInfoProvider {

    public static DeviceInfoDTO getDeviceInfo() {
        return PlatformEngine.getInstance().getDeviceService().getDeviceInfo();
    }

    public static void setScreenMetrics(int width, int height, float density) {
        PlatformEngine.getInstance().getDeviceService().setScreenMetrics(width, height, density);
    }
}
