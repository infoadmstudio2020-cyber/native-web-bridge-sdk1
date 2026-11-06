package com.roni.library.theme;

import com.roni.library.contracts.api.IApiCallback;
import com.roni.library.contracts.api.NativeApiError;
import com.roni.library.contracts.api.NativeApiRequest;
import com.roni.library.contracts.api.NativeApiResponse;
import com.roni.library.theme.handler.ThemeCommandHandler;
import com.roni.library.theme.manager.ThemeManager;
import org.junit.Assert;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class ThemeCommandHandlerTest {

    @Test
    public void testThemeCommandHandlerRoutes() {
        ThemeManager manager = new ThemeManager();
        ThemeCommandHandler handler = new ThemeCommandHandler(manager);

        Assert.assertEquals("theme", handler.getPath());
        Assert.assertEquals("theme", handler.getRoutePrefix());
        Assert.assertEquals("*", handler.getAction());

        // Test getTheme
        NativeApiRequest getReq = new NativeApiRequest("req-1", "theme", "getTheme", new HashMap<>(), System.currentTimeMillis());
        NativeApiResponse getResp = handler.handleRequest(getReq);
        Assert.assertTrue(getResp.isSuccess());
        Assert.assertNotNull(getResp.getData());
        Assert.assertTrue(getResp.getData().containsKey("themeName"));

        // Test setThemeMode
        Map<String, Object> modeParams = new HashMap<>();
        modeParams.put("mode", "DARK");
        NativeApiRequest setModeReq = new NativeApiRequest("req-2", "theme", "setThemeMode", modeParams, System.currentTimeMillis());
        NativeApiResponse setModeResp = handler.handleRequest(setModeReq);
        Assert.assertTrue(setModeResp.isSuccess());
        Assert.assertEquals("DARK", setModeResp.getData().get("mode"));
        Assert.assertTrue(manager.getActiveTheme().isDarkMode());

        // Test isSystemDark
        NativeApiRequest isDarkReq = new NativeApiRequest("req-3", "theme", "isSystemDark", new HashMap<>(), System.currentTimeMillis());
        NativeApiResponse isDarkResp = handler.handleRequest(isDarkReq);
        Assert.assertTrue(isDarkResp.isSuccess());

        // Test getCssVariables
        NativeApiRequest cssReq = new NativeApiRequest("req-4", "theme", "getCssVariables", new HashMap<>(), System.currentTimeMillis());
        NativeApiResponse cssResp = handler.handleRequest(cssReq);
        Assert.assertTrue(cssResp.isSuccess());
        Assert.assertTrue(cssResp.getData().get("css").toString().contains(":root"));
    }

    @Test
    public void testAsyncCallbackInterface() {
        ThemeManager manager = new ThemeManager();
        ThemeCommandHandler handler = new ThemeCommandHandler(manager);

        NativeApiRequest req = new NativeApiRequest("req-cb", "theme", "getTheme", new HashMap<>(), System.currentTimeMillis());
        final AtomicReference<NativeApiResponse> respRef = new AtomicReference<>();
        final AtomicReference<NativeApiError> errRef = new AtomicReference<>();

        handler.handle(req, new IApiCallback() {
            @Override
            public void onResponse(NativeApiResponse response) {
                respRef.set(response);
            }

            @Override
            public void onError(NativeApiError error) {
                errRef.set(error);
            }
        });

        Assert.assertNotNull(respRef.get());
        Assert.assertNull(errRef.get());
    }
}
