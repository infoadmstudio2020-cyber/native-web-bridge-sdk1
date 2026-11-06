package com.roni.library.platform.listener;

import com.roni.library.platform.model.NetworkStatusDTO;

/**
 * Listener interface for network state change notifications.
 *
 * @author infoadmstudio2020-cyber
 */
public interface NetworkListener {

    /**
     * Called when network connectivity, network type, or signal strength changes.
     *
     * @param status Updated network status
     */
    void onNetworkChanged(NetworkStatusDTO status);
}
