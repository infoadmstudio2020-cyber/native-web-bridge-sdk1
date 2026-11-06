package com.roni.library.platform.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Data Transfer Object representing device hardware and OS telemetry.
 *
 * @author infoadmstudio2020-cyber
 */
public class DeviceInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String manufacturer;
    private final String model;
    private final String brand;
    private final String device;
    private final String osVersion;
    private final int sdkInt;
    private final int screenWidth;
    private final int screenHeight;
    private final float screenDensity;
    private final String architecture;
    private final long totalMemoryBytes;
    private final long freeMemoryBytes;

    public DeviceInfoDTO(String manufacturer, String model, String brand, String device,
                         String osVersion, int sdkInt, int screenWidth, int screenHeight,
                         float screenDensity, String architecture, long totalMemoryBytes, long freeMemoryBytes) {
        this.manufacturer = manufacturer != null ? manufacturer : "Generic";
        this.model = model != null ? model : "Standard Device";
        this.brand = brand != null ? brand : "Android";
        this.device = device != null ? device : "arm64";
        this.osVersion = osVersion != null ? osVersion : "14.0";
        this.sdkInt = sdkInt > 0 ? sdkInt : 34;
        this.screenWidth = screenWidth > 0 ? screenWidth : 1080;
        this.screenHeight = screenHeight > 0 ? screenHeight : 2400;
        this.screenDensity = screenDensity > 0 ? screenDensity : 2.75f;
        this.architecture = architecture != null ? architecture : "arm64-v8a";
        this.totalMemoryBytes = totalMemoryBytes > 0 ? totalMemoryBytes : Runtime.getRuntime().totalMemory();
        this.freeMemoryBytes = freeMemoryBytes > 0 ? freeMemoryBytes : Runtime.getRuntime().freeMemory();
    }

    public static DeviceInfoDTO createDefault() {
        Runtime rt = Runtime.getRuntime();
        return new DeviceInfoDTO(
                "Google",
                "Pixel 8 Pro",
                "google",
                "husky",
                "14.0",
                34,
                1080,
                2400,
                2.75f,
                System.getProperty("os.arch", "aarch64"),
                rt.totalMemory(),
                rt.freeMemory()
        );
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public String getBrand() {
        return brand;
    }

    public String getDevice() {
        return device;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public int getSdkInt() {
        return sdkInt;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public float getScreenDensity() {
        return screenDensity;
    }

    public String getArchitecture() {
        return architecture;
    }

    public long getTotalMemoryBytes() {
        return totalMemoryBytes;
    }

    public long getFreeMemoryBytes() {
        return freeMemoryBytes;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("manufacturer", manufacturer);
        map.put("model", model);
        map.put("brand", brand);
        map.put("device", device);
        map.put("osVersion", osVersion);
        map.put("sdkInt", sdkInt);
        map.put("screenWidth", screenWidth);
        map.put("screenHeight", screenHeight);
        map.put("screenDensity", screenDensity);
        map.put("architecture", architecture);
        map.put("totalMemoryBytes", totalMemoryBytes);
        map.put("freeMemoryBytes", freeMemoryBytes);
        return map;
    }

    @Override
    public String toString() {
        return "DeviceInfoDTO{" +
                "manufacturer='" + manufacturer + '\'' +
                ", model='" + model + '\'' +
                ", brand='" + brand + '\'' +
                ", osVersion='" + osVersion + '\'' +
                ", sdkInt=" + sdkInt +
                ", screen=" + screenWidth + "x" + screenHeight + "@" + screenDensity +
                '}';
    }
}
