package com.roni.library.platform;

import com.roni.library.platform.model.BatteryStatusDTO;
import com.roni.library.platform.model.DeviceInfoDTO;
import com.roni.library.platform.model.NetworkStatusDTO;
import com.roni.library.platform.model.NetworkType;
import com.roni.library.platform.model.StorageStatsDTO;
import com.roni.library.platform.model.ToastRecord;
import com.roni.library.platform.service.BatteryPlatformService;
import com.roni.library.platform.service.ClipboardPlatformService;
import com.roni.library.platform.service.DevicePlatformService;
import com.roni.library.platform.service.NetworkPlatformService;
import com.roni.library.platform.service.StoragePlatformService;
import com.roni.library.platform.service.ToastNotificationService;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

/**
 * Unit tests covering all individual platform subsystem services.
 *
 * @author infoadmstudio2020-cyber
 */
public class PlatformServicesTest {

    @Test
    public void testBatteryPlatformService() {
        BatteryPlatformService service = new BatteryPlatformService();
        assertNotNull(service.getBatteryStatus());

        AtomicBoolean listenerNotified = new AtomicBoolean(false);
        service.addListener(status -> {
            listenerNotified.set(true);
            assertEquals(92, status.getLevel());
            assertTrue(status.isCharging());
        });

        service.updateBattery(92, true, "AC", 31.0f, "GOOD");
        assertTrue(listenerNotified.get());

        BatteryStatusDTO current = service.getBatteryStatus();
        assertEquals(92, current.getLevel());
        assertTrue(current.isCharging());
        assertEquals("AC", current.getPlugType());
    }

    @Test
    public void testClipboardPlatformService() {
        ClipboardPlatformService service = new ClipboardPlatformService();
        assertFalse(service.hasText());

        service.setText("sample_token_abc");
        assertTrue(service.hasText());
        assertEquals("sample_token_abc", service.getText());

        service.clear();
        assertFalse(service.hasText());
        assertEquals("", service.getText());
    }

    @Test
    public void testDevicePlatformService() {
        DevicePlatformService service = new DevicePlatformService();
        DeviceInfoDTO info = service.getDeviceInfo();
        assertNotNull(info);
        assertTrue(info.getScreenWidth() > 0);
        assertTrue(info.getScreenHeight() > 0);

        service.setScreenMetrics(1440, 3120, 3.5f);
        DeviceInfoDTO updated = service.getDeviceInfo();
        assertEquals(1440, updated.getScreenWidth());
        assertEquals(3120, updated.getScreenHeight());
        assertEquals(3.5f, updated.getScreenDensity(), 0.001f);
    }

    @Test
    public void testNetworkPlatformService() {
        NetworkPlatformService service = new NetworkPlatformService();
        assertNotNull(service.getNetworkType());

        AtomicBoolean notified = new AtomicBoolean(false);
        service.addListener(status -> {
            notified.set(true);
            assertEquals(NetworkType.CELLULAR, status.getNetworkType());
            assertTrue(status.isMetered());
        });

        service.updateNetworkState(NetworkType.CELLULAR, true, true, -75);
        assertTrue(notified.get());
        assertEquals(NetworkType.CELLULAR, service.getNetworkType());
        assertTrue(service.isConnected());
        assertTrue(service.isMetered());
    }

    @Test
    public void testStoragePlatformService() {
        StoragePlatformService service = new StoragePlatformService();
        StorageStatsDTO stats = service.getStorageStatsDTO();
        assertNotNull(stats);
        assertTrue(stats.getInternalTotalBytes() > 0);
        assertTrue(service.clearCache());
    }

    @Test
    public void testToastNotificationService() {
        ToastNotificationService service = new ToastNotificationService();
        AtomicBoolean listenerCalled = new AtomicBoolean(false);

        service.addListener(record -> {
            listenerCalled.set(true);
            assertEquals("Hello Platform", record.getMessage());
            assertFalse(record.isLong());
        });

        service.showToast("Hello Platform", false);
        assertTrue(listenerCalled.get());
        assertEquals("Hello Platform", service.getMessage());

        List<ToastRecord> history = service.getToastHistory();
        assertFalse(history.isEmpty());
        assertEquals("Hello Platform", history.get(history.size() - 1).getMessage());

        service.vibrate(150);
        assertEquals(150, service.getLastVibrationMs());
    }
}
