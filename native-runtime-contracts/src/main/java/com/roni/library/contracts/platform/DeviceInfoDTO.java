package com.roni.library.contracts.platform;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.Serializable;

/**
 * Universal Data Transfer Object for device hardware and operating system telemetry.
 *
 * @author infoadmstudio2020-cyber
 */
public final class DeviceInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String manufacturer;
    private final String model;
    private final String osVersion;
    private final int sdkInt;
    private final String deviceUuid;
    private final long totalMemory;

    public DeviceInfoDTO(@NonNull String manufacturer,
                         @NonNull String model,
                         @NonNull String osVersion,
                         int sdkInt,
                         @Nullable String deviceUuid,
                         long totalMemory) {
        this.manufacturer = manufacturer != null ? manufacturer : "Unknown";
        this.model = model != null ? model : "Unknown";
        this.osVersion = osVersion != null ? osVersion : "Unknown";
        this.sdkInt = sdkInt;
        this.deviceUuid = deviceUuid != null ? deviceUuid : "";
        this.totalMemory = totalMemory;
    }

    @NonNull
    public String getManufacturer() {
        return manufacturer;
    }

    @NonNull
    public String getModel() {
        return model;
    }

    @NonNull
    public String getOsVersion() {
        return osVersion;
    }

    public int getSdkInt() {
        return sdkInt;
    }

    @NonNull
    public String getDeviceUuid() {
        return deviceUuid;
    }

    public long getTotalMemory() {
        return totalMemory;
    }

    @NonNull
    @Override
    public String toString() {
        return "DeviceInfoDTO{manufacturer='" + manufacturer + "', model='" + model + "', osVersion='" + osVersion + "', sdkInt=" + sdkInt + "}";
    }
}
