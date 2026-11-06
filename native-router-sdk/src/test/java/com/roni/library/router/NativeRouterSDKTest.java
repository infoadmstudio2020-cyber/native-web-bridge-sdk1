package com.roni.library.router;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link NativeRouterSDK}, {@link NativeRouterSDKConfig}, and {@link NativeRouterSDKResult}.
 *
 * @author infoadmstudio2020-cyber
 */
public class NativeRouterSDKTest {

    @Before
    public void setUp() {
        NativeRouter.getInstance().initialize();
    }

    @After
    public void tearDown() {
        NativeRouter.getInstance().release();
    }

    @Test
    public void testConfigBuilderDefaults() {
        NativeRouterSDKConfig config = new NativeRouterSDKConfig.Builder().build();
        assertNotNull(config);
        assertFalse(config.isDebugEnabled());
        assertEquals("NativeRouterSDK", config.getTag());
        assertEquals(30000, config.getTimeout());
    }

    @Test
    public void testConfigBuilderCustomValues() {
        NativeRouterSDKConfig config = new NativeRouterSDKConfig.Builder()
                .debugEnabled(true)
                .tag("CustomRouterTag")
                .timeout(5000)
                .build();

        assertNotNull(config);
        assertTrue(config.isDebugEnabled());
        assertEquals("CustomRouterTag", config.getTag());
        assertEquals(5000, config.getTimeout());
    }

    @Test
    public void testResultSuccess() {
        String testData = "payload_123";
        NativeRouterSDKResult<String> result = NativeRouterSDKResult.success(testData);

        assertTrue(result.isSuccess());
        assertEquals(testData, result.getData());
        assertNull(result.getErrorMessage());
        assertEquals(0, result.getErrorCode());
        assertTrue(result.toString().contains("payload_123"));
    }

    @Test
    public void testResultError() {
        NativeRouterSDKResult<Void> result = NativeRouterSDKResult.error(404, "Entity Not Found");

        assertFalse(result.isSuccess());
        assertNull(result.getData());
        assertEquals("Entity Not Found", result.getErrorMessage());
        assertEquals(404, result.getErrorCode());
    }

    @Test
    public void testRouterSingletonTransitions() {
        NativeRouter router = NativeRouter.getInstance();
        assertNotNull(router);
        assertTrue(router.isInitialized());

        router.release();
        assertTrue(router.isDisposed());

        // Calling getInstance again returns a fresh active instance
        NativeRouter newRouter = NativeRouter.getInstance();
        assertNotNull(newRouter);
        assertTrue(newRouter.isInitialized());
        assertFalse(newRouter.isDisposed());
    }
}

