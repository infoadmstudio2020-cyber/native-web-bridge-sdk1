package com.roni.library.platform.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import com.roni.library.platform.listener.BatteryListener;
import com.roni.library.platform.model.BatteryStatusDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Subsystem service for querying and monitoring battery telemetry.
 *
 * @author infoadmstudio2020-cyber
 */
public class BatteryPlatformService {

    private final AtomicReference<BatteryStatusDTO> currentStatus = new AtomicReference<>(BatteryStatusDTO.createDefault());
    private final List<BatteryListener> listeners = new CopyOnWriteArrayList<>();
    private Context context;
    private BroadcastReceiver batteryReceiver;

    public BatteryPlatformService() {
    }

    public void init(Context context) {
        if (context == null) return;
        this.context = context.getApplicationContext();

        try {
            IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryIntent = this.context.registerReceiver(null, filter);
            if (batteryIntent != null) {
                parseBatteryIntent(batteryIntent);
            }

            batteryReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context ctx, Intent intent) {
                    parseBatteryIntent(intent);
                }
            };
            this.context.registerReceiver(batteryReceiver, filter);
        } catch (Throwable ignored) {
            // Fallback to simulated state on headless or permission-restricted environments
        }
    }

    private void parseBatteryIntent(Intent intent) {
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        int percent = scale > 0 ? (int) ((level / (float) scale) * 100) : 80;

        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean charging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        String plugType = "BATTERY";
        if (plugged == BatteryManager.BATTERY_PLUGGED_AC) plugType = "AC";
        else if (plugged == BatteryManager.BATTERY_PLUGGED_USB) plugType = "USB";
        else if (plugged == BatteryManager.BATTERY_PLUGGED_WIRELESS) plugType = "WIRELESS";

        int tempTenths = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 250);
        float temperature = tempTenths / 10.0f;

        int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, BatteryManager.BATTERY_HEALTH_GOOD);
        String healthStr = "GOOD";
        if (health == BatteryManager.BATTERY_HEALTH_OVERHEAT) healthStr = "OVERHEAT";
        else if (health == BatteryManager.BATTERY_HEALTH_DEAD) healthStr = "DEAD";
        else if (health == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE) healthStr = "OVER_VOLTAGE";

        updateBattery(percent, charging, plugType, temperature, healthStr);
    }

    public BatteryStatusDTO getBatteryStatus() {
        return currentStatus.get();
    }

    public Map<String, Object> getExtendedBatteryInfo() {
        Map<String, Object> map = new HashMap<>(currentStatus.get().toMap());
        map.put("activeListeners", listeners.size());
        map.put("hasNativeBinding", batteryReceiver != null);
        return map;
    }

    public synchronized void updateBattery(int newLevel, boolean charging, String plugType, float temp, String healthStatus) {
        BatteryStatusDTO updated = new BatteryStatusDTO(newLevel, charging, plugType, temp, healthStatus, System.currentTimeMillis());
        currentStatus.set(updated);

        for (BatteryListener listener : listeners) {
            try {
                listener.onBatteryChanged(updated);
            } catch (Throwable ignored) {
            }
        }
    }

    public void addListener(BatteryListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(BatteryListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    public void release() {
        if (context != null && batteryReceiver != null) {
            try {
                context.unregisterReceiver(batteryReceiver);
            } catch (Throwable ignored) {
            }
            batteryReceiver = null;
        }
        listeners.clear();
    }
}
