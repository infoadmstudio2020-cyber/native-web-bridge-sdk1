package com.roni.library.sdk;

import com.roni.library.contracts.api.NativeApiRequest;
import com.roni.library.contracts.api.NativeApiResponse;
import com.roni.library.contracts.common.RuntimeState;
import com.roni.library.sdk.bootstrap.NativeRuntimeBootstrap;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class NativeRuntimeSDKTest {

    private NativeRuntime runtime;

    @Before
    public void setUp() {
        runtime = NativeRuntime.getInstance();
        runtime.release();
    }

    @After
    public void tearDown() {
        if (runtime != null) {
            runtime.release();
        }
    }

    @Test
    public void testSingletonLifecycleAndInitialization() {
        Assert.assertFalse(runtime.isInitialized());
        Assert.assertEquals(RuntimeState.UNINITIALIZED, runtime.getState());

        RuntimeConfiguration config = RuntimeConfiguration.builder()
                .appId("com.roni.test.app")
                .environment("TESTING")
                .debugEnabled(true)
                .build();

        NativeRuntimeResult<Boolean> initResult = runtime.init(null, config);
        Assert.assertTrue(initResult.isSuccess());
        Assert.assertTrue(runtime.isInitialized());
        Assert.assertEquals(RuntimeState.READY, runtime.getState());
        Assert.assertTrue(runtime.isHealthy());
        Assert.assertNotNull(runtime.getSession());
        Assert.assertEquals(RuntimeSession.State.ACTIVE, runtime.getSession().getState());

        // Re-init guard test
        NativeRuntimeResult<Boolean> secondInit = runtime.init(null, config);
        Assert.assertTrue(secondInit.isSuccess());

        runtime.release();
        Assert.assertFalse(runtime.isInitialized());
        Assert.assertEquals(RuntimeState.UNINITIALIZED, runtime.getState());
        Assert.assertNull(runtime.getSession());
    }

    @Test
    public void testRuntimeConfigurationBuilder() {
        RuntimeConfiguration config = RuntimeConfiguration.builder()
                .appId("com.test.bounds")
                .environment("STAGING")
                .debugEnabled(true)
                .logLevel("VERBOSE")
                .storageEncryptionEnabled(true)
                .networkTimeoutMs(30000)
                .maxNetworkRetries(5)
                .telemetryEnabled(true)
                .addCustomProperty("region", "US-EAST")
                .build();

        Assert.assertEquals("com.test.bounds", config.getAppId());
        Assert.assertEquals("STAGING", config.getEnvironment());
        Assert.assertTrue(config.isDebugEnabled());
        Assert.assertEquals("VERBOSE", config.getLogLevel());
        Assert.assertTrue(config.isStorageEncryptionEnabled());
        Assert.assertEquals(30000, config.getNetworkTimeoutMs());
        Assert.assertEquals(5, config.getMaxNetworkRetries());
        Assert.assertTrue(config.isTelemetryEnabled());
        Assert.assertEquals("US-EAST", config.getCustomProperties().get("region"));
    }

    @Test
    public void testRuntimeBuilderFluent() {
        NativeRuntime rt = RuntimeBuilder.with(null)
                .appId("com.fluent.app")
                .debugEnabled(true)
                .initialize();

        Assert.assertTrue(rt.isInitialized());
        Assert.assertEquals("com.fluent.app", rt.getConfiguration().getAppId());
        rt.release();
    }

    @Test
    public void test10StepBootstrapSequence() {
        RuntimeConfiguration config = RuntimeConfiguration.builder()
                .appId("com.bootstrap.test")
                .build();

        NativeRuntimeBootstrap.BootstrapReport report = NativeRuntimeBootstrap.execute(null, config);
        Assert.assertNotNull(report);
        Assert.assertTrue(report.isOverallSuccess());

        List<NativeRuntimeBootstrap.StepReport> steps = report.getSteps();
        Assert.assertEquals(10, steps.size());
        Assert.assertEquals(10, report.getPassedCount());

        Assert.assertEquals("Contracts & ABI Foundation", steps.get(0).getStepName());
        Assert.assertEquals("L0", steps.get(0).getLayer());
        Assert.assertTrue(steps.get(0).isSuccess());

        Assert.assertEquals("Logging Subsystem (NativeLoggerSDK)", steps.get(1).getStepName());
        Assert.assertEquals("Service Directory (NativeServiceSDK)", steps.get(2).getStepName());
        Assert.assertEquals("Security Engine (NativeSecurityEngine)", steps.get(3).getStepName());
        Assert.assertEquals("Storage Engine (NativeStorageEngine)", steps.get(4).getStepName());
        Assert.assertEquals("Platform Engine (NativePlatformEngine)", steps.get(5).getStepName());
        Assert.assertEquals("Network Engine (NativeNetworkEngine)", steps.get(6).getStepName());
        Assert.assertEquals("Analytics Engine (NativeAnalyticsEngine)", steps.get(7).getStepName());
        Assert.assertEquals("Theme & DevTools (NativeThemeEngine & DevTools)", steps.get(8).getStepName());
        Assert.assertEquals("Router, Bridge & Runtime (L3/L4 Master)", steps.get(9).getStepName());
    }

    @Test
    public void testRuntimeSessionLifecycle() {
        runtime.init(null);
        RuntimeSession session = runtime.getSession();
        Assert.assertNotNull(session);
        Assert.assertNotNull(session.getSessionId());
        Assert.assertEquals(RuntimeSession.State.ACTIVE, session.getState());

        runtime.pauseSession();
        Assert.assertEquals(RuntimeSession.State.PAUSED, session.getState());
        Assert.assertEquals(RuntimeState.SUSPENDED, runtime.getState());

        runtime.resumeSession();
        Assert.assertEquals(RuntimeSession.State.ACTIVE, session.getState());
        Assert.assertEquals(RuntimeState.READY, runtime.getState());

        runtime.release();
        Assert.assertNull(runtime.getSession());
    }

    @Test
    public void testLifecycleObserverCallbacks() {
        final AtomicBoolean initNotified = new AtomicBoolean(false);
        final AtomicBoolean pauseNotified = new AtomicBoolean(false);
        final AtomicBoolean resumeNotified = new AtomicBoolean(false);
        final AtomicBoolean releaseNotified = new AtomicBoolean(false);

        LifecycleObserver observer = new LifecycleObserver() {
            @Override public void onInitialized(NativeRuntime r) { initNotified.set(true); }
            @Override public void onSessionStarted(RuntimeSession s) {}
            @Override public void onSessionPaused(RuntimeSession s) { pauseNotified.set(true); }
            @Override public void onSessionResumed(RuntimeSession s) { resumeNotified.set(true); }
            @Override public void onSessionStopped(RuntimeSession s) {}
            @Override public void onReleased() { releaseNotified.set(true); }
            @Override public void onError(int code, String msg) {}
        };

        runtime.addObserver(observer);
        runtime.init(null);
        Assert.assertTrue(initNotified.get());

        runtime.pauseSession();
        Assert.assertTrue(pauseNotified.get());

        runtime.resumeSession();
        Assert.assertTrue(resumeNotified.get());

        runtime.release();
        Assert.assertTrue(releaseNotified.get());
    }

    @Test
    public void testResultWrapper() {
        NativeRuntimeResult<String> success = NativeRuntimeResult.success("Payload");
        Assert.assertTrue(success.isSuccess());
        Assert.assertEquals("Payload", success.getData());
        Assert.assertEquals(0, success.getErrorCode());

        NativeRuntimeResult<String> fail = NativeRuntimeResult.failure(404, "Not Found");
        Assert.assertFalse(fail.isSuccess());
        Assert.assertEquals(404, fail.getErrorCode());
        Assert.assertEquals("Not Found", fail.getErrorMessage());
    }

    @Test
    public void testApiRequestDispatch() {
        runtime.init(null);
        Map<String, Object> params = new HashMap<>();
        params.put("target", "healthCheck");

        NativeApiRequest request = new NativeApiRequest("req-123", "system", "ping", params, System.currentTimeMillis());
        NativeApiResponse response = runtime.executeApiRequest(request);

        Assert.assertNotNull(response);
        Assert.assertTrue(response.isSuccess());
        Assert.assertEquals("req-123", response.getRequestId());
        Assert.assertNotNull(response.getData());
        Assert.assertEquals("NativeRuntimeMasterFacade", response.getData().get("handledBy"));
    }

    @Test
    public void testTelemetryTracking() {
        runtime.init(null);
        Map<String, Object> props = new HashMap<>();
        props.put("screen", "DashboardActivity");
        props.put("user_tier", "premium");

        runtime.trackEvent("screen_view", props);
        Map<String, Object> events = runtime.getTelemetryEvents();
        Assert.assertEquals(1, events.size());
    }
}
