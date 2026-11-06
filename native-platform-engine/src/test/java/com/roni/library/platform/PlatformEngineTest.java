package com.roni.library.platform;

import com.roni.library.contracts.common.RuntimeState;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link PlatformEngine}, {@link PlatformEngineConfig}, and {@link PlatformEngineResult}.
 *
 * @author infoadmstudio2020-cyber
 */
public class PlatformEngineTest {

    private PlatformEngine engine;

    @Before
    public void setUp() {
        engine = new PlatformEngine();
        engine.initialize();
    }

    @After
    public void tearDown() {
        if (engine != null) {
            engine.release();
        }
    }

    @Test
    public void testEngineLifecycle() {
        assertTrue(engine.isInitialized());
        assertEquals(RuntimeState.READY, engine.getState());
        assertFalse(engine.isDisposed());

        engine.release();
        assertTrue(engine.isDisposed());
        assertEquals(RuntimeState.TERMINATED, engine.getState());
        assertFalse(engine.isInitialized());
    }

    @Test
    public void testConfigBuilder() {
        PlatformEngineConfig config = new PlatformEngineConfig.Builder()
                .debugEnabled(true)
                .tag("CustomTag")
                .autoMonitorBattery(false)
                .autoMonitorNetwork(false)
                .build();

        assertNotNull(config);
        assertTrue(config.isDebugEnabled());
        assertEquals("CustomTag", config.getTag());
        assertFalse(config.isAutoMonitorBattery());
        assertFalse(config.isAutoMonitorNetwork());
    }

    @Test
    public void testResultContainerSuccess() {
        String payload = "sensor_data_42";
        PlatformEngineResult<String> result = PlatformEngineResult.success(payload);

        assertTrue(result.isSuccess());
        assertEquals(payload, result.getData());
        assertNull(result.getErrorMessage());
        assertEquals(0, result.getErrorCode());
    }

    @Test
    public void testResultContainerError() {
        PlatformEngineResult<Void> result = PlatformEngineResult.error(503, "Service Unavailable");

        assertFalse(result.isSuccess());
        assertNull(result.getData());
        assertEquals("Service Unavailable", result.getErrorMessage());
        assertEquals(503, result.getErrorCode());
    }

    @Test
    public void testSubServicesNotNull() {
        assertNotNull(engine.getDeviceService());
        assertNotNull(engine.getBatteryService());
        assertNotNull(engine.getNetworkService());
        assertNotNull(engine.getStorageService());
        assertNotNull(engine.getClipboardService());
        assertNotNull(engine.getToastService());
        assertNotNull(engine.getCommandHandler());
    }
}
