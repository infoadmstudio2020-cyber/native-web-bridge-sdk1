package com.roni.library.security.permission;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Subsystem service for managing and querying Android runtime permissions.
 *
 * @author infoadmstudio2020-cyber
 */
public class PermissionService {

    private final Map<String, PermissionStatus> permissionCache = new ConcurrentHashMap<>();
    private final List<PermissionCallback> globalListeners = new CopyOnWriteArrayList<>();
    private PermissionDelegate permissionDelegate;

    public void setPermissionDelegate(PermissionDelegate delegate) {
        this.permissionDelegate = delegate;
    }

    public PermissionStatus checkPermission(String permission) {
        if (permission == null) return PermissionStatus.NOT_REQUESTED;
        return permissionCache.getOrDefault(permission, PermissionStatus.GRANTED);
    }

    public boolean isPermissionGranted(String permission) {
        return checkPermission(permission).isGranted();
    }

    public void setPermissionStatus(String permission, PermissionStatus status) {
        if (permission != null && status != null) {
            permissionCache.put(permission, status);
        }
    }

    public boolean shouldShowRequestPermissionRationale(String permission) {
        if (permissionDelegate != null) {
            return permissionDelegate.shouldShowRationale(permission);
        }
        return false;
    }

    public void requestPermission(String permission, PermissionCallback callback) {
        requestMultiplePermissions(Collections.singletonList(permission), callback);
    }

    public void requestMultiplePermissions(List<String> permissions, PermissionCallback callback) {
        if (permissions == null || permissions.isEmpty()) {
            if (callback != null) {
                callback.onMultiplePermissionsResult(Collections.emptyMap());
            }
            return;
        }

        if (permissionDelegate != null) {
            permissionDelegate.requestPermissions(permissions, callback);
        } else {
            // Simulated/Headless fallback for JVM unit testing and background daemons
            Map<String, PermissionStatus> results = new HashMap<>();
            for (String p : permissions) {
                PermissionStatus current = permissionCache.getOrDefault(p, PermissionStatus.GRANTED);
                results.put(p, current);
                onSinglePermissionResult(p, current);
            }
            onMultiplePermissionsResult(results);
            if (callback != null) {
                if (permissions.size() == 1) {
                    String p = permissions.get(0);
                    callback.onSinglePermissionResult(p, results.get(p));
                }
                callback.onMultiplePermissionsResult(results);
            }
        }
    }

    public void onSinglePermissionResult(String permission, PermissionStatus status) {
        if (permission != null && status != null) {
            permissionCache.put(permission, status);
            for (PermissionCallback listener : globalListeners) {
                try {
                    listener.onSinglePermissionResult(permission, status);
                } catch (Exception ignored) {}
            }
        }
    }

    public void onMultiplePermissionsResult(Map<String, PermissionStatus> results) {
        if (results != null) {
            permissionCache.putAll(results);
            for (PermissionCallback listener : globalListeners) {
                try {
                    listener.onMultiplePermissionsResult(results);
                } catch (Exception ignored) {}
            }
        }
    }

    public void addListener(PermissionCallback listener) {
        if (listener != null && !globalListeners.contains(listener)) {
            globalListeners.add(listener);
        }
    }

    public void removeListener(PermissionCallback listener) {
        if (listener != null) {
            globalListeners.remove(listener);
        }
    }

    public void clear() {
        permissionCache.clear();
        globalListeners.clear();
        permissionDelegate = null;
    }
}
