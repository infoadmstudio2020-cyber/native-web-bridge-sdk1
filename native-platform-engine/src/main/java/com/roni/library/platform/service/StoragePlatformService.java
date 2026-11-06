package com.roni.library.platform.service;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import com.roni.library.platform.model.StorageStatsDTO;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Subsystem service for storage telemetry and cache management.
 *
 * @author infoadmstudio2020-cyber
 */
public class StoragePlatformService {

    private Context context;

    public StoragePlatformService() {
    }

    public void init(Context context) {
        if (context == null) return;
        this.context = context.getApplicationContext();
    }

    public StorageStatsDTO getStorageStatsDTO() {
        long internalTotal = 0;
        long internalFree = 0;
        long cacheSize = 0;
        long externalTotal = 0;
        long externalFree = 0;

        try {
            File dataDir = Environment.getDataDirectory();
            if (dataDir != null && dataDir.exists()) {
                StatFs stat = new StatFs(dataDir.getPath());
                internalTotal = stat.getTotalBytes();
                internalFree = stat.getAvailableBytes();
            }

            if (context != null) {
                File cacheDir = context.getCacheDir();
                if (cacheDir != null && cacheDir.exists()) {
                    cacheSize = getFolderSize(cacheDir);
                }
            }

            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                File extDir = Environment.getExternalStorageDirectory();
                if (extDir != null && extDir.exists()) {
                    StatFs extStat = new StatFs(extDir.getPath());
                    externalTotal = extStat.getTotalBytes();
                    externalFree = extStat.getAvailableBytes();
                }
            }
        } catch (Throwable ignored) {
            return StorageStatsDTO.createDefault();
        }

        if (internalTotal <= 0) {
            return StorageStatsDTO.createDefault();
        }

        return new StorageStatsDTO(internalTotal, internalFree, cacheSize, externalTotal, externalFree);
    }

    public Map<String, Object> getStorageStats() {
        return getStorageStatsDTO().toMap();
    }

    public synchronized boolean clearCache() {
        if (context == null) {
            return true;
        }

        try {
            File cacheDir = context.getCacheDir();
            if (cacheDir != null && cacheDir.exists()) {
                deleteDir(cacheDir);
            }
            File extCacheDir = context.getExternalCacheDir();
            if (extCacheDir != null && extCacheDir.exists()) {
                deleteDir(extCacheDir);
            }
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    private boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                for (String child : children) {
                    boolean success = deleteDir(new File(dir, child));
                    if (!success) return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        }
        return false;
    }

    private long getFolderSize(File file) {
        long size = 0;
        if (file == null || !file.exists()) return 0;
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    size += getFolderSize(f);
                }
            }
        } else {
            size = file.length();
        }
        return size;
    }

    public void release() {
        context = null;
    }
}
