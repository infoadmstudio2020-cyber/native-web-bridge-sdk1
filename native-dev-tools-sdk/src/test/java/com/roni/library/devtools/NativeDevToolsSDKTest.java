package com.roni.library.devtools;

import com.roni.library.contracts.api.IApiCallback;
import com.roni.library.contracts.api.NativeApiError;
import com.roni.library.contracts.api.NativeApiRequest;
import com.roni.library.contracts.api.NativeApiResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Unit tests for {@link NativeDevToolsSDK} and {@link DevToolsSDK}.
 */
public class NativeDevToolsSDKTest {

    @Before
    @After
    public void tearDown() {
        NativeDevToolsSDK.getInstance().release();
    }

    @Test
    public void testSingletonLifecycleAndConfig() {
        NativeDevToolsSDK sdk = NativeDevToolsSDK.getInstance();
        Assert.assertFalse(sdk.isInitialized());

        NativeDevToolsSDKConfig config = NativeDevToolsSDKConfig.builder()
                .debugEnabled(true)
                .tag("CustomDevTools")
                .timeout(10000)
                .serverPort(9225)
                .floatingBubbleEnabled(true)
                .shakeDetectionEnabled(true)
                .build();

        NativeDevToolsSDKResult<Boolean> initResult = sdk.init(null, config);
        Assert.assertTrue(initResult.isSuccess());
        Assert.assertTrue(sdk.isInitialized());
        Assert.assertEquals("CustomDevTools", sdk.getConfig().getTag());
        Assert.assertEquals(9225, sdk.getConfig().getServerPort());

        // Test static facade
        Assert.assertTrue(DevToolsSDK.isInitialized());
        Assert.assertNotNull(DevToolsSDK.getDebugDrawer());
        Assert.assertNotNull(DevToolsSDK.getNetworkInspector());
        Assert.assertNotNull(DevToolsSDK.getFpsMonitor());
        Assert.assertNotNull(DevToolsSDK.getCrashReporter());

        DevToolsSDK.release();
        Assert.assertFalse(DevToolsSDK.isInitialized());
    }

    @Test
    public void testCommandHandlerRoutes() {
        NativeDevToolsSDK sdk = NativeDevToolsSDK.getInstance();
        sdk.init(null, NativeDevToolsSDKConfig.defaultConfiguration());

        NativeApiRequest req = new NativeApiRequest("req-metrics", "devtools", "getMetrics", new HashMap<>(), System.currentTimeMillis());
        NativeApiResponse resp = sdk.getCommandHandler().handleRequest(req);
        Assert.assertTrue(resp.isSuccess());

        final AtomicReference<NativeApiResponse> cbResp = new AtomicReference<>();
        sdk.getCommandHandler().handle(req, new IApiCallback() {
            @Override
            public void onResponse(NativeApiResponse response) {
                cbResp.set(response);
            }

            @Override
            public void onError(NativeApiError error) {
            }
        });
        Assert.assertNotNull(cbResp.get());
    }

    @Test
    public void testResultModels() {
        NativeDevToolsSDKResult<String> res = NativeDevToolsSDKResult.success("Ok");
        Assert.assertTrue(res.isSuccess());
        Assert.assertEquals("Ok", res.getData());

        NativeDevToolsSDKResult<String> fail = NativeDevToolsSDKResult.failure("Failed", 500);
        Assert.assertFalse(fail.isSuccess());
        Assert.assertEquals(500, fail.getErrorCode());
        Assert.assertEquals("Failed", fail.getErrorMessage());
    }
}

