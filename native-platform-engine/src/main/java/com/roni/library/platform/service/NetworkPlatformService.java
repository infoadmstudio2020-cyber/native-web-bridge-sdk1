package com.roni.library.platform.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;

import com.roni.library.platform.listener.NetworkListener;
import com.roni.library.platform.model.NetworkStatusDTO;
import com.roni.library.platform.model.NetworkType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Subsystem service for connectivity monitoring (WiFi, Cellular, VPN, Offline).
 *
 * @author infoadmstudio2020-cyber
 */
public class NetworkPlatformService {

    private final AtomicReference<NetworkStatusDTO> currentStatus = new AtomicReference<>(NetworkStatusDTO.createDefault());
    private final List<NetworkListener> listeners = new CopyOnWriteArrayList<>();
    private Context context;
    private ConnectivityManager connectivityManager;
    private ConnectivityManager.NetworkCallback networkCallback;

    public NetworkPlatformService() {
    }

    public void init(Context context) {
        if (context == null) return;
        this.context = context.getApplicationContext();

        try {
            connectivityManager = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                readCurrentNetworkCapabilities();
                registerNetworkCallback();
            }
        } catch (Throwable ignored) {
            // Retain simulated state for headless/test environments
        }
    }

    private void readCurrentNetworkCapabilities() {
        if (connectivityManager == null) return;
        try {
            Network active = connectivityManager.getActiveNetwork();
            if (active == null) {
                updateNetworkState(NetworkType.NONE, false, false, -100);
                return;
            }

            NetworkCapabilities caps = connectivityManager.getNetworkCapabilities(active);
            if (caps == null) {
                updateNetworkState(NetworkType.NONE, false, false, -100);
                return;
            }

            boolean connected = caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
            boolean metered = !caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED);

            NetworkType type = NetworkType.UNKNOWN;
            if (caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                type = NetworkType.WIFI;
            } else if (caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                type = NetworkType.CELLULAR;
            } else if (caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                type = NetworkType.ETHERNET;
            } else if (caps.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
                type = NetworkType.VPN;
            } else if (caps.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)) {
                type = NetworkType.BLUETOOTH;
            }

            updateNetworkState(type, connected, metered, -60);
        } catch (Throwable ignored) {
        }
    }

    private void registerNetworkCallback() {
        if (connectivityManager == null) return;
        try {
            NetworkRequest request = new NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .build();

            networkCallback = new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(Network network) {
                    readCurrentNetworkCapabilities();
                }

                @Override
                public void onLost(Network network) {
                    updateNetworkState(NetworkType.NONE, false, false, -100);
                }

                @Override
                public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
                    readCurrentNetworkCapabilities();
                }
            };
            connectivityManager.registerNetworkCallback(request, networkCallback);
        } catch (Throwable ignored) {
        }
    }

    public NetworkType getNetworkType() {
        return currentStatus.get().getNetworkType();
    }

    public boolean isConnected() {
        return currentStatus.get().isConnected();
    }

    public boolean isMetered() {
        return currentStatus.get().isMetered();
    }

    public Map<String, Object> getNetworkStatus() {
        Map<String, Object> map = new HashMap<>(currentStatus.get().toMap());
        map.put("activeListeners", listeners.size());
        map.put("hasNativeBinding", networkCallback != null);
        return map;
    }

    public synchronized void updateNetworkState(NetworkType type, boolean connected, boolean metered, int signalDbm) {
        NetworkStatusDTO updated = new NetworkStatusDTO(type, connected, metered, signalDbm, System.currentTimeMillis());
        currentStatus.set(updated);

        for (NetworkListener listener : listeners) {
            try {
                listener.onNetworkChanged(updated);
            } catch (Throwable ignored) {
            }
        }
    }

    public void addListener(NetworkListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(NetworkListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    public void release() {
        if (connectivityManager != null && networkCallback != null) {
            try {
                connectivityManager.unregisterNetworkCallback(networkCallback);
            } catch (Throwable ignored) {
            }
            networkCallback = null;
        }
        listeners.clear();
        context = null;
    }
}
