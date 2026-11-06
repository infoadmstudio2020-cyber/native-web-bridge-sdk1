package com.roni.library.platform;

import com.roni.library.platform.model.BatteryStatusDTO;
import com.roni.library.platform.model.DeviceInfoDTO;
import com.roni.library.platform.model.NetworkStatusDTO;
import com.roni.library.platform.model.NetworkType;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Unit tests for convenience providers and monitors:
 * {@link DeviceInfoProvider}, {@link BatteryMonitor}, {@link NetworkStateMonitor}, and {@link SensorHub}.
 *
 * @author infoadmstudio2020-cyber
 */
public class MonitorsAndHubTest {

    @Before
    public void setUp() {
        PlatformEngine.getInstance().initialize();
    }

    @Test
    public void testDeviceInfoProvider() {
        DeviceInfoDTO info = DeviceInfoProvider.getDeviceInfo();
        assertNotNull(info);
        assertNotNull(info.getManufacturer());

        DeviceInfoProvider.setScreenMetrics(720, 1280, 2.0f);
        assertEquals(720, DeviceInfoProvider.getDeviceInfo().getScreenWidth());
    }

    @Test
    public void testBatteryMonitor() {
        BatteryStatusDTO battery = BatteryMonitor.getBatteryStatus();
        assertNotNull(battery);
        assertTrue(battery.getLevel() >= 0 && battery.getLevel() <= 100);
    }

    @Test
    public void testNetworkStateMonitor() {
        assertNotNull(NetworkStateMonitor.getNetworkType());
        NetworkStatusDTO status = NetworkStateMonitor.getNetworkStatus();
        assertNotNull(status);
    }

    @Test
    public void testSensorHub() {
        assertTrue(SensorHub.isSensorAvailable("accelerometer"));
        assertTrue(SensorHub.isSensorAvailable("gyroscope"));
        assertFalse(SensorHub.isSensorAvailable("non_existent_sensor"));

        Map<String, Object> telemetry = SensorHub.getSensorTelemetry();
        assertNotNull(telemetry);
        assertTrue(telemetry.containsKey("availableSensors"));
        assertTrue((int) telemetry.get("totalSensors") > 0);
    }
}
