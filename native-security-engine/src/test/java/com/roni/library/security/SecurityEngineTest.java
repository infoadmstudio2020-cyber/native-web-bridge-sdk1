package com.roni.library.security;

import com.roni.library.contracts.common.RuntimeState;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link SecurityEngine}, {@link SecurityEngineConfig}, and {@link SecurityEngineResult}.
 *
 * @author infoadmstudio2020-cyber
 */
public class SecurityEngineTest {

    private SecurityEngine engine;

    @Before
    public void setUp() {
        engine = new SecurityEngine();
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
        SecurityEngineConfig config = new SecurityEngineConfig.Builder()
                .debugEnabled(true)
                .tag("CustomSecurity")
                .requireHardwareBackedKeys(true)
                .biometricFallbackEnabled(false)
                .build();

        assertNotNull(config);
        assertTrue(config.isDebugEnabled());
        assertEquals("CustomSecurity", config.getTag());
        assertTrue(config.isRequireHardwareBackedKeys());
        assertFalse(config.isBiometricFallbackEnabled());
    }

    @Test
    public void testResultContainerSuccess() {
        SecurityEngineResult<String> result = SecurityEngineResult.success("token_123");
        assertTrue(result.isSuccess());
        assertEquals("token_123", result.getData());
        assertNull(result.getErrorMessage());
        assertEquals(0, result.getErrorCode());
    }

    @Test
    public void testResultContainerError() {
        SecurityEngineResult<Void> result = SecurityEngineResult.error(401, "Unauthorized");
        assertFalse(result.isSuccess());
        assertNull(result.getData());
        assertEquals("Unauthorized", result.getErrorMessage());
        assertEquals(401, result.getErrorCode());
    }

    @Test
    public void testSubsystemsNotNull() {
        assertNotNull(engine.getKeyStoreManager());
        assertNotNull(engine.getBiometricService());
        assertNotNull(engine.getPermissionService());
        assertNotNull(engine.getCommandHandler());
    }
}
