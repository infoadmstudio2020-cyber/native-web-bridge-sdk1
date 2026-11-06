package com.roni.library.platform;

import com.roni.library.platform.listener.NetworkListener;
import com.roni.library.platform.model.NetworkStatusDTO;
import com.roni.library.platform.model.NetworkType;

/**
 * Convenience monitor class for real-time network connectivity changes.
 *
 * @author infoadmstudio2020-cyber
 */
public class NetworkStateMonitor {

    public static NetworkType getNetworkType() {
        return PlatformEngine.getInstance().getNetworkService().getNetworkType();
    }

    public static boolean isConnected() {
        return PlatformEngine.getInstance().getNetworkService().isConnected();
    }

    public static NetworkStatusDTO getNetworkStatus() {
        return new NetworkStatusDTO(
                PlatformEngine.getInstance().getNetworkService().getNetworkType(),
                PlatformEngine.getInstance().getNetworkService().isConnected(),
                PlatformEngine.getInstance().getNetworkService().isMetered(),
                -60,
                System.currentTimeMillis()
        );
    }

    public static void addListener(NetworkListener listener) {
        PlatformEngine.getInstance().getNetworkService().addListener(listener);
    }

    public static void removeListener(NetworkListener listener) {
        PlatformEngine.getInstance().getNetworkService().removeListener(listener);
    }
}
