package com.roni.library.platform.service;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import com.roni.library.platform.model.DeviceInfoDTO;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Subsystem service providing device specifications, screen metrics, and OS details.
 *
 * @author infoadmstudio2020-cyber
 */
public class DevicePlatformService {

    private final AtomicReference<DeviceInfoDTO> deviceInfo = new AtomicReference<>(DeviceInfoDTO.createDefault());
    private Context context;

    public DevicePlatformService() {
    }

    public void init(Context context) {
        if (context == null) return;
        this.context = context.getApplicationContext();

        try {
            Resources res = this.context.getResources();
            DisplayMetrics dm = res.getDisplayMetrics();

            Runtime rt = Runtime.getRuntime();
            DeviceInfoDTO nativeInfo = new DeviceInfoDTO(
                    Build.MANUFACTURER,
                    Build.MODEL,
                    Build.BRAND,
                    Build.DEVICE,
                    Build.VERSION.RELEASE,
                    Build.VERSION.SDK_INT,
                    dm.widthPixels,
                    dm.heightPixels,
                    dm.density,
                    Build.SUPPORTED_ABIS != null && Build.SUPPORTED_ABIS.length > 0 ? Build.SUPPORTED_ABIS[0] : "arm64-v8a",
                    rt.totalMemory(),
                    rt.freeMemory()
            );
            deviceInfo.set(nativeInfo);
        } catch (Throwable ignored) {
            // Retain simulated default
        }
    }

    public DeviceInfoDTO getDeviceInfo() {
        return deviceInfo.get();
    }

    public void setScreenMetrics(int width, int height, float density) {
        DeviceInfoDTO current = deviceInfo.get();
        DeviceInfoDTO updated = new DeviceInfoDTO(
                current.getManufacturer(),
                current.getModel(),
                current.getBrand(),
                current.getDevice(),
                current.getOsVersion(),
                current.getSdkInt(),
                width,
                height,
                density,
                current.getArchitecture(),
                current.getTotalMemoryBytes(),
                current.getFreeMemoryBytes()
        );
        deviceInfo.set(updated);
    }

    public Map<String, Object> getExtendedMetrics() {
        Map<String, Object> map = new HashMap<>(deviceInfo.get().toMap());
        map.put("availableProcessors", Runtime.getRuntime().availableProcessors());
        map.put("maxMemoryBytes", Runtime.getRuntime().maxMemory());
        map.put("hasContextBinding", context != null);
        return map;
    }

    public void release() {
        context = null;
    }
}
