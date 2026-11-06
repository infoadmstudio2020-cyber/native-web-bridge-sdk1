package com.roni.library.platform.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Data Transfer Object representing internal and external storage metrics.
 *
 * @author infoadmstudio2020-cyber
 */
public class StorageStatsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private final long internalTotalBytes;
    private final long internalFreeBytes;
    private final long internalUsedBytes;
    private final long cacheSizeBytes;
    private final long externalTotalBytes;
    private final long externalFreeBytes;

    public StorageStatsDTO(long internalTotalBytes, long internalFreeBytes, long cacheSizeBytes,
                           long externalTotalBytes, long externalFreeBytes) {
        this.internalTotalBytes = internalTotalBytes;
        this.internalFreeBytes = internalFreeBytes;
        this.internalUsedBytes = Math.max(0, internalTotalBytes - internalFreeBytes);
        this.cacheSizeBytes = cacheSizeBytes;
        this.externalTotalBytes = externalTotalBytes;
        this.externalFreeBytes = externalFreeBytes;
    }

    public static StorageStatsDTO createDefault() {
        long total = 128L * 1024 * 1024 * 1024; // 128 GB
        long free = 64L * 1024 * 1024 * 1024;   // 64 GB
        long cache = 512L * 1024 * 1024;        // 512 MB
        return new StorageStatsDTO(total, free, cache, total, free);
    }

    public long getInternalTotalBytes() {
        return internalTotalBytes;
    }

    public long getInternalFreeBytes() {
        return internalFreeBytes;
    }

    public long getInternalUsedBytes() {
        return internalUsedBytes;
    }

    public long getCacheSizeBytes() {
        return cacheSizeBytes;
    }

    public long getExternalTotalBytes() {
        return externalTotalBytes;
    }

    public long getExternalFreeBytes() {
        return externalFreeBytes;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("internalTotalBytes", internalTotalBytes);
        map.put("internalFreeBytes", internalFreeBytes);
        map.put("internalUsedBytes", internalUsedBytes);
        map.put("cacheSizeBytes", cacheSizeBytes);
        map.put("externalTotalBytes", externalTotalBytes);
        map.put("externalFreeBytes", externalFreeBytes);
        return map;
    }

    @Override
    public String toString() {
        return "StorageStatsDTO{" +
                "internalTotal=" + (internalTotalBytes / 1024 / 1024) + "MB" +
                ", internalFree=" + (internalFreeBytes / 1024 / 1024) + "MB" +
                ", cacheSize=" + (cacheSizeBytes / 1024 / 1024) + "MB" +
                '}';
    }
}
