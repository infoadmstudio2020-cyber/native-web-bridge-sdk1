package com.roni.library.platform.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Data Transfer Object representing network status and connectivity telemetry.
 *
 * @author infoadmstudio2020-cyber
 */
public class NetworkStatusDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private final NetworkType networkType;
    private final boolean connected;
    private final boolean metered;
    private final int signalDbm;
    private final long timestamp;

    public NetworkStatusDTO(NetworkType networkType, boolean connected, boolean metered, int signalDbm, long timestamp) {
        this.networkType = networkType != null ? networkType : NetworkType.NONE;
        this.connected = connected;
        this.metered = metered;
        this.signalDbm = signalDbm;
        this.timestamp = timestamp > 0 ? timestamp : System.currentTimeMillis();
    }

    public static NetworkStatusDTO createDefault() {
        return new NetworkStatusDTO(NetworkType.WIFI, true, false, -55, System.currentTimeMillis());
    }

    public NetworkType getNetworkType() {
        return networkType;
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isMetered() {
        return metered;
    }

    public int getSignalDbm() {
        return signalDbm;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("networkType", networkType.name());
        map.put("connected", connected);
        map.put("metered", metered);
        map.put("signalDbm", signalDbm);
        map.put("timestamp", timestamp);
        return map;
    }

    @Override
    public String toString() {
        return "NetworkStatusDTO{" +
                "networkType=" + networkType +
                ", connected=" + connected +
                ", metered=" + metered +
                ", signalDbm=" + signalDbm +
                ", timestamp=" + timestamp +
                '}';
    }
}
