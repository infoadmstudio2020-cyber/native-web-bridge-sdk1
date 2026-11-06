package com.roni.library.runtime;

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
 * Unit tests for {@link NativeRuntimeEngine} and {@link RuntimeEngine}.
 */
public class NativeRuntimeEngineTest {

    @Before
    @After
    public void tearDown() {
        NativeRuntimeEngine.getInstance().release();
    }

    @Test
    public void testSingletonLifecycleAndConfig() {
        NativeRuntimeEngine engine = NativeRuntimeEngine.getInstance();
        Assert.assertFalse(engine.isInitialized());

        NativeRuntimeEngineConfig config = NativeRuntimeEngineConfig.builder()
                .debugEnabled(true)
                .tag("CustomRuntimeEngine")
                .timeout(15000)
                .localServerPort(8888)
                .cspStrictEnabled(true)
                .allowMixedContent(false)
                .hardwareAccelerationEnabled(true)
                .defaultUrl("http://127.0.0.1:8888/index.html")
                .build();

        NativeRuntimeEngineResult<Boolean> initResult = engine.init(null, config);
        Assert.assertTrue(initResult.isSuccess());
        Assert.assertTrue(engine.isInitialized());
        Assert.assertEquals("CustomRuntimeEngine", engine.getConfig().getTag());
        Assert.assertEquals(8888, engine.getConfig().getLocalServerPort());

        // Test static facade
        Assert.assertTrue(RuntimeEngine.isInitialized());
        Assert.assertNotNull(RuntimeEngine.getConfigurator());
        Assert.assertNotNull(RuntimeEngine.getInterceptor());
        Assert.assertNotNull(RuntimeEngine.getCookieSyncManager());

        RuntimeEngine.release();
        Assert.assertFalse(RuntimeEngine.isInitialized());
    }

    @Test
    public void testCommandHandlerRoutes() {
        NativeRuntimeEngine engine = NativeRuntimeEngine.getInstance();
        engine.init(null, NativeRuntimeEngineConfig.defaultConfiguration());

        HashMap<String, Object> params = new HashMap<>();
        params.put("url", "https://app.local/dashboard");

        NativeApiRequest req = new NativeApiRequest("req-1", "runtime", "loadUrl", params, System.currentTimeMillis());
        NativeApiResponse resp = engine.getCommandHandler().handleRequest(req);
        Assert.assertTrue(resp.isSuccess());

        final AtomicReference<NativeApiResponse> cbResp = new AtomicReference<>();
        engine.getCommandHandler().handle(req, new IApiCallback() {
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
        NativeRuntimeEngineResult<String> res = NativeRuntimeEngineResult.success("Success Data");
        Assert.assertTrue(res.isSuccess());
        Assert.assertEquals("Success Data", res.getData());

        NativeRuntimeEngineResult<String> fail = NativeRuntimeEngineResult.failure("Failed Error", 500);
        Assert.assertFalse(fail.isSuccess());
        Assert.assertEquals(500, fail.getErrorCode());
        Assert.assertEquals("Failed Error", fail.getErrorMessage());

        RuntimeEngineResult<Integer> rAlias = RuntimeEngineResult.success(100);
        Assert.assertTrue(rAlias.isSuccess());
        Assert.assertEquals(100, (int) rAlias.getData());
    }
}
