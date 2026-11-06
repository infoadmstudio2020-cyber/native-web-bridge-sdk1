package com.roni.library.ui;

import com.roni.library.contracts.api.IApiCallback;
import com.roni.library.contracts.api.NativeApiError;
import com.roni.library.contracts.api.NativeApiRequest;
import com.roni.library.contracts.api.NativeApiResponse;
import com.roni.library.ui.theme.UIThemeBridge;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Unit tests for {@link NativeRuntimeUI}, {@link NativeUIHost}, and {@link RuntimeUI}.
 */
public class NativeRuntimeUITest {

    @Before
    @After
    public void tearDown() {
        NativeRuntimeUI.getInstance().release();
    }

    @Test
    public void testSingletonLifecycleAndConfig() {
        NativeRuntimeUI ui = NativeRuntimeUI.getInstance();
        Assert.assertFalse(ui.isInitialized());

        NativeRuntimeUIConfig config = NativeRuntimeUIConfig.builder()
                .debugEnabled(true)
                .tag("CustomRuntimeUI")
                .timeout(12000)
                .animationDurationMs(250)
                .bottomSheetDismissible(true)
                .autoDismissToasts(true)
                .build();

        NativeRuntimeUIResult<Boolean> initResult = ui.init(null, config);
        Assert.assertTrue(initResult.isSuccess());
        Assert.assertTrue(ui.isInitialized());
        Assert.assertEquals("CustomRuntimeUI", ui.getConfig().getTag());
        Assert.assertEquals(250, ui.getConfig().getAnimationDurationMs());

        // Test static facades
        Assert.assertTrue(NativeUIHost.isInitialized());
        Assert.assertNotNull(NativeUIHost.getToast());
        Assert.assertNotNull(NativeUIHost.getLoadingIndicator());
        Assert.assertNotNull(NativeUIHost.getBottomSheet());
        Assert.assertNotNull(NativeUIHost.getActionSheet());

        Assert.assertTrue(RuntimeUI.isInitialized());

        RuntimeUI.release();
        Assert.assertFalse(NativeRuntimeUI.getInstance().isInitialized());
    }

    @Test
    public void testCommandHandlerRoutes() {
        NativeRuntimeUI ui = NativeRuntimeUI.getInstance();
        ui.init(null, NativeRuntimeUIConfig.defaultConfiguration());

        HashMap<String, Object> params = new HashMap<>();
        params.put("title", "Modal Title");
        params.put("message", "Modal Body");

        NativeApiRequest req = new NativeApiRequest("req-alert", "ui", "showAlert", params, System.currentTimeMillis());
        NativeApiResponse resp = ui.getCommandHandler().handleRequest(req);
        Assert.assertTrue(resp.isSuccess());

        final AtomicReference<NativeApiResponse> cbResp = new AtomicReference<>();
        ui.getCommandHandler().handle(req, new IApiCallback() {
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
    public void testUIThemeBridge() {
        Map<String, Object> colors = UIThemeBridge.getActiveThemeColors();
        Assert.assertNotNull(colors);
        Assert.assertTrue(colors.containsKey("primaryColor"));
        Assert.assertTrue(colors.containsKey("surfaceColor"));
    }

    @Test
    public void testResultModels() {
        NativeRuntimeUIResult<String> res = NativeRuntimeUIResult.success("UI Ready");
        Assert.assertTrue(res.isSuccess());
        Assert.assertEquals("UI Ready", res.getData());

        NativeRuntimeUIResult<String> fail = NativeRuntimeUIResult.failure("UI Failed", 400);
        Assert.assertFalse(fail.isSuccess());
        Assert.assertEquals(400, fail.getErrorCode());
        Assert.assertEquals("UI Failed", fail.getErrorMessage());

        NativeUIHostResult<Integer> hostResult = NativeUIHostResult.success(99);
        Assert.assertTrue(hostResult.isSuccess());
        Assert.assertEquals(99, (int) hostResult.getData());
    }
}
