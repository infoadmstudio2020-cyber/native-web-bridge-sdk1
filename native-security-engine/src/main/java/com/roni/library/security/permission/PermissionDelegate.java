package com.roni.library.security.permission;

import java.util.List;

/**
 * Delegate interface for requesting runtime permissions from Android Activity.
 *
 * @author infoadmstudio2020-cyber
 */
public interface PermissionDelegate {

    void requestPermissions(List<String> permissions, PermissionCallback callback);

    boolean shouldShowRationale(String permission);
}
