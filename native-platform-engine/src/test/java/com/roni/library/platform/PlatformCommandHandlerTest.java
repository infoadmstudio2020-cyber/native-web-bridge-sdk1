package com.roni.library.platform;

import com.roni.library.contracts.api.IApiCallback;
import com.roni.library.contracts.api.NativeApiError;
import com.roni.library.contracts.api.NativeApiRequest;
import com.roni.library.contracts.api.NativeApiResponse;
import com.roni.library.platform.handler.PlatformCommandHandler;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link PlatformCommandHandler} Layer 0 RPC contract dispatching.
 *
 * @author infoadmstudio2020-cyber
 */
public class PlatformCommandHandlerTest {

    private PlatformCommandHandler handler;

    @Before
    public void setUp() {
        PlatformEngine.getInstance().initialize();
        handler = new PlatformCommandHandler();
    }

    @Test
    public void testRoutePrefix() {
        assertEquals("/platform", handler.getRoutePrefix());
        assertEquals("/platform", handler.getPath());
        assertEquals("*", handler.getAction());
    }

    @Test
    public void testDeviceInfoCommand() {
        NativeApiRequest request = new NativeApiRequest("req_dev", "/platform", "device.info", new HashMap<>(), System.currentTimeMillis());
        NativeApiResponse response = handler.handleRequest(request);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("req_dev", response.getRequestId());
        assertTrue(response.getData().containsKey("deviceInfo"));
    }

    @Test
    public void testBatteryStatusCommand() {
        NativeApiRequest request = new NativeApiRequest("req_bat", "/platform", "battery.status", new HashMap<>(), System.currentTimeMillis());
        NativeApiResponse response = handler.handleRequest(request);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertTrue(response.getData().containsKey("batteryStatus"));
    }

    @Test
    public void testNetworkStatusCommand() {
        NativeApiRequest request = new NativeApiRequest("req_net", "/platform", "network.status", new HashMap<>(), System.currentTimeMillis());
        NativeApiResponse response = handler.handleRequest(request);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertTrue(response.getData().containsKey("networkStatus"));
    }

    @Test
    public void testClipboardCommands() {
        Map<String, Object> setParams = new HashMap<>();
        setParams.put("text", "rpc_clipboard_test");
        NativeApiRequest setRequest = new NativeApiRequest("req_clip_set", "/platform", "clipboard.set", setParams, System.currentTimeMillis());
        NativeApiResponse setResponse = handler.handleRequest(setRequest);

        assertTrue(setResponse.isSuccess());

        NativeApiRequest getRequest = new NativeApiRequest("req_clip_get", "/platform", "clipboard.get", new HashMap<>(), System.currentTimeMillis());
        NativeApiResponse getResponse = handler.handleRequest(getRequest);

        assertTrue(getResponse.isSuccess());
        assertEquals("rpc_clipboard_test", getResponse.getData().get("clipboardText"));
    }

    @Test
    public void testToastAndVibrateCommands() {
        Map<String, Object> toastParams = new HashMap<>();
        toastParams.put("message", "Test Toast");
        NativeApiRequest toastReq = new NativeApiRequest("req_toast", "/platform", "toast.show", toastParams, System.currentTimeMillis());
        NativeApiResponse toastRes = handler.handleRequest(toastReq);
        assertTrue(toastRes.isSuccess());

        Map<String, Object> vibParams = new HashMap<>();
        vibParams.put("duration", 250);
        NativeApiRequest vibReq = new NativeApiRequest("req_vib", "/platform", "toast.vibrate", vibParams, System.currentTimeMillis());
        NativeApiResponse vibRes = handler.handleRequest(vibReq);
        assertTrue(vibRes.isSuccess());
        assertEquals(250L, vibRes.getData().get("durationMs"));
    }

    @Test
    public void testDiagnosticDefaultCommand() {
        NativeApiRequest request = new NativeApiRequest("req_diag", "/platform", "diagnostic", new HashMap<>(), System.currentTimeMillis());
        NativeApiResponse response = handler.handleRequest(request);

        assertTrue(response.isSuccess());
        assertTrue(response.getData().containsKey("device"));
        assertTrue(response.getData().containsKey("battery"));
        assertTrue(response.getData().containsKey("network"));
        assertTrue(response.getData().containsKey("storage"));
    }

    @Test
    public void testAsyncHandleCallback() {
        AtomicBoolean called = new AtomicBoolean(false);
        NativeApiRequest request = new NativeApiRequest("req_async", "/platform", "device.info", new HashMap<>(), System.currentTimeMillis());

        handler.handle(request, new IApiCallback() {
            @Override
            public void onResponse(NativeApiResponse response) {
                called.set(true);
                assertTrue(response.isSuccess());
            }

            @Override
            public void onError(NativeApiError error) {
            }
        });

        assertTrue(called.get());
    }
}
