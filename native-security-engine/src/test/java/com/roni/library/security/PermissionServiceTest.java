package com.roni.library.security;

import com.roni.library.security.permission.PermissionCallback;
import com.roni.library.security.permission.PermissionService;
import com.roni.library.security.permission.PermissionStatus;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link PermissionService}.
 *
 * @author infoadmstudio2020-cyber
 */
public class PermissionServiceTest {

    private PermissionService service;

    @Before
    public void setUp() {
        service = new PermissionService();
    }

    @Test
    public void testPermissionStatusFlow() {
        String perm = "android.permission.CAMERA";
        service.setPermissionStatus(perm, PermissionStatus.DENIED);
        assertEquals(PermissionStatus.DENIED, service.checkPermission(perm));
        assertFalse(service.isPermissionGranted(perm));

        service.setPermissionStatus(perm, PermissionStatus.GRANTED);
        assertEquals(PermissionStatus.GRANTED, service.checkPermission(perm));
        assertTrue(service.isPermissionGranted(perm));
    }

    @Test
    public void testRequestMultiplePermissions() {
        service.setPermissionStatus("android.permission.RECORD_AUDIO", PermissionStatus.GRANTED);
        service.setPermissionStatus("android.permission.ACCESS_FINE_LOCATION", PermissionStatus.DENIED);

        AtomicBoolean callbackReceived = new AtomicBoolean(false);
        service.requestMultiplePermissions(
                Arrays.asList("android.permission.RECORD_AUDIO", "android.permission.ACCESS_FINE_LOCATION"),
                new PermissionCallback() {
                    @Override
                    public void onSinglePermissionResult(String permission, PermissionStatus status) {}

                    @Override
                    public void onMultiplePermissionsResult(Map<String, PermissionStatus> results) {
                        callbackReceived.set(true);
                        assertEquals(PermissionStatus.GRANTED, results.get("android.permission.RECORD_AUDIO"));
                        assertEquals(PermissionStatus.DENIED, results.get("android.permission.ACCESS_FINE_LOCATION"));
                    }
                }
        );

        assertTrue(callbackReceived.get());
    }
}
