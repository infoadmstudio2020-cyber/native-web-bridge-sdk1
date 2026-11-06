package com.roni.library.security.permission;

import java.util.Map;

/**
 * Callback for receiving permission request results.
 *
 * @author infoadmstudio2020-cyber
 */
public interface PermissionCallback {

    void onSinglePermissionResult(String permission, PermissionStatus status);

    void onMultiplePermissionsResult(Map<String, PermissionStatus> results);
}
