package com.roni.library.contracts;

import com.roni.library.contracts.analytics.AnalyticsEvent;
import com.roni.library.contracts.analytics.PerformanceMetric;
import com.roni.library.contracts.api.IApiHandler;
import com.roni.library.contracts.api.NativeApiError;
import com.roni.library.contracts.api.NativeApiRequest;
import com.roni.library.contracts.api.NativeApiResponse;
import com.roni.library.contracts.common.ExecutionContext;
import com.roni.library.contracts.common.RuntimeState;
import com.roni.library.contracts.interfaces.INativeRuntimeContractsListener;
import com.roni.library.contracts.logger.LogEntry;
import com.roni.library.contracts.logger.LogLevel;
import com.roni.library.contracts.platform.BatteryStatusDTO;
import com.roni.library.contracts.platform.DeviceInfoDTO;
import com.roni.library.contracts.runtime.WebSourceType;
import com.roni.library.contracts.ui.DialogConfig;
import com.roni.library.contracts.ui.ToolbarConfig;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

/**
 * Comprehensive JVM Unit Tests for {@link NativeRuntimeContracts} and Foundation Contracts.
 *
 * @author infoadmstudio2020-cyber
 */
public class NativeRuntimeContractsTest {

    private NativeRuntimeContracts mContracts;

    @Before
    public void setUp() {
        mContracts = NativeRuntimeContracts.getInstance();
        mContracts.release();
        mContracts = NativeRuntimeContracts.getInstance();
    }

    @After
    public void tearDown() {
        if (mContracts != null) {
            mContracts.release();
        }
    }

    @Test
    public void getInstance_returnsSameInstance() {
        NativeRuntimeContracts a = NativeRuntimeContracts.getInstance();
        NativeRuntimeContracts b = NativeRuntimeContracts.getInstance();
        assertSame(a, b);
    }

    @Test
    public void initialState_isUninitialized() {
        assertEquals(RuntimeState.UNINITIALIZED, mContracts.getState());
        assertFalse(mContracts.isInitialized());
        assertNull(mContracts.getConfig());
    }

    @Test
    public void initWithNullContext_returnsError() {
        final AtomicBoolean errorReceived = new AtomicBoolean(false);
        mContracts.addListener(new INativeRuntimeContractsListener() {
            @Override
            public void onInitialized() {}

            @Override
            public void onError(int errorCode, String message) {
                if (errorCode == -101) {
                    errorReceived.set(true);
                }
            }
        });

        NativeRuntimeContractsConfig config = new NativeRuntimeContractsConfig.Builder().build();
        NativeRuntimeContractsResult<RuntimeState> result = mContracts.init(null, config);

        assertFalse(result.isSuccess());
        assertEquals(-101, result.getErrorCode());
        assertTrue(errorReceived.get());
        assertEquals(RuntimeState.UNINITIALIZED, mContracts.getState());
    }

    @Test
    public void configBuilder_defaultsAndCustomValues() {
        NativeRuntimeContractsConfig defaultConfig = new NativeRuntimeContractsConfig.Builder().build();
        assertFalse(defaultConfig.isDebugEnabled());
        assertEquals("NativeRuntimeContracts", defaultConfig.getTag());
        assertEquals(30_000, defaultConfig.getTimeout());

        NativeRuntimeContractsConfig customConfig = new NativeRuntimeContractsConfig.Builder()
                .debugEnabled(true)
                .tag("CustomTag")
                .timeout(15_000)
                .build();
        assertTrue(customConfig.isDebugEnabled());
        assertEquals("CustomTag", customConfig.getTag());
        assertEquals(15_000, customConfig.getTimeout());
        assertTrue(customConfig.toString().contains("CustomTag"));
    }

    @Test
    public void nativeRuntimeContractsResult_successAndError() {
        NativeRuntimeContractsResult<String> successResult = NativeRuntimeContractsResult.success("Payload");
        assertTrue(successResult.isSuccess());
        assertEquals("Payload", successResult.getData());
        assertNull(successResult.getErrorMessage());
        assertEquals(0, successResult.getErrorCode());

        NativeRuntimeContractsResult<Void> emptySuccess = NativeRuntimeContractsResult.success();
        assertTrue(emptySuccess.isSuccess());
        assertNull(emptySuccess.getData());

        NativeRuntimeContractsResult<String> errorResult = NativeRuntimeContractsResult.error("Failure", 500);
        assertFalse(errorResult.isSuccess());
        assertNull(errorResult.getData());
        assertEquals("Failure", errorResult.getErrorMessage());
        assertEquals(500, errorResult.getErrorCode());
    }

    @Test
    public void base64EncodingAndDecoding_roundTrip() {
        String original = "NativeRuntime Ecosystem Universal Contracts Test 2026!";
        byte[] bytes = original.getBytes(StandardCharsets.UTF_8);

        String encoded = NativeRuntimeContracts.encodeBase64(bytes);
        assertNotNull(encoded);
        assertFalse(encoded.isEmpty());

        byte[] decoded = NativeRuntimeContracts.decodeBase64(encoded);
        String restored = new String(decoded, StandardCharsets.UTF_8);
        assertEquals(original, restored);
    }

    @Test
    public void apiRequestAndResponse_dispatching() {
        mContracts.registerApiHandler(new IApiHandler() {
            @Override
            public String getPath() {
                return "echo";
            }

            @Override
            public String getAction() {
                return "ping";
            }

            @Override
            public NativeApiResponse handleRequest(NativeApiRequest request) {
                Map<String, Object> data = new HashMap<>();
                data.put("echo", request.getStringParam("msg", "pong"));
                return NativeApiResponse.success(request.getRequestId(), data);
            }
        });

        NativeApiRequest request = new NativeApiRequest.Builder("echo", "ping")
                .param("msg", "hello_contracts")
                .build();

        NativeApiResponse response = mContracts.dispatchApiRequest(request);
        assertTrue(response.isSuccess());
        assertEquals(200, response.getStatusCode());
        assertEquals("hello_contracts", response.getData().get("echo"));

        // Test unhandled request returns 404
        NativeApiRequest unknownRequest = new NativeApiRequest.Builder("unknown", "action").build();
        NativeApiResponse unknownResponse = mContracts.dispatchApiRequest(unknownRequest);
        assertFalse(unknownResponse.isSuccess());
        assertEquals(404, unknownResponse.getStatusCode());
    }

    @Test
    public void runtimeState_checks() {
        assertFalse(RuntimeState.UNINITIALIZED.isTerminal());
        assertFalse(RuntimeState.UNINITIALIZED.isOperational());

        assertFalse(RuntimeState.INITIALIZING.isTerminal());
        assertFalse(RuntimeState.INITIALIZING.isOperational());

        assertFalse(RuntimeState.READY.isTerminal());
        assertTrue(RuntimeState.READY.isOperational());

        assertFalse(RuntimeState.SUSPENDED.isTerminal());
        assertFalse(RuntimeState.SUSPENDED.isOperational());

        assertTrue(RuntimeState.TERMINATED.isTerminal());
        assertFalse(RuntimeState.TERMINATED.isOperational());
    }

    @Test
    public void logLevel_priorityAndParsing() {
        assertTrue(LogLevel.ERROR.isLoggable(LogLevel.INFO));
        assertTrue(LogLevel.INFO.isLoggable(LogLevel.DEBUG));
        assertFalse(LogLevel.DEBUG.isLoggable(LogLevel.WARN));
        assertFalse(LogLevel.NONE.isLoggable(LogLevel.DEBUG));

        assertEquals(LogLevel.DEBUG, LogLevel.fromString("debug", LogLevel.INFO));
        assertEquals(LogLevel.ERROR, LogLevel.fromString("ERROR", LogLevel.INFO));
        assertEquals(LogLevel.INFO, LogLevel.fromString("INVALID_LEVEL", LogLevel.INFO));
        assertEquals(LogLevel.WARN, LogLevel.fromString(null, LogLevel.WARN));
    }

    @Test
    public void logEntry_fields() {
        LogEntry entry = new LogEntry(LogLevel.WARN, "TestTag", "Warning message");
        assertEquals(LogLevel.WARN, entry.getLevel());
        assertEquals("TestTag", entry.getTag());
        assertEquals("Warning message", entry.getMessage());
        assertNotNull(entry.getThreadName());
        assertTrue(entry.toString().contains("TestTag"));
    }

    @Test
    public void analyticsEventAndPerformanceMetric() {
        Map<String, Object> params = new HashMap<>();
        params.put("screen", "MainActivity");
        AnalyticsEvent event = new AnalyticsEvent("screen_view", params);
        assertEquals("screen_view", event.getName());
        assertEquals("MainActivity", event.getParameter("screen"));
        assertTrue(event.getTimestamp() > 0);

        PerformanceMetric metric = new PerformanceMetric("app_start", 245.5, "ms");
        assertEquals("app_start", metric.getMetricName());
        assertEquals(245.5, metric.getValue(), 0.001);
        assertEquals("ms", metric.getUnit());
    }

    @Test
    public void platformDTOs_getters() {
        BatteryStatusDTO battery = new BatteryStatusDTO(85, true, "AC", 28.5f);
        assertEquals(85, battery.getLevel());
        assertTrue(battery.isCharging());
        assertEquals("AC", battery.getPluggedType());
        assertEquals(28.5f, battery.getTemperature(), 0.01f);

        DeviceInfoDTO device = new DeviceInfoDTO("Google", "Pixel 8", "Android 14", 34, "uuid-1234", 8589934592L);
        assertEquals("Google", device.getManufacturer());
        assertEquals("Pixel 8", device.getModel());
        assertEquals("Android 14", device.getOsVersion());
        assertEquals(34, device.getSdkInt());
        assertEquals("uuid-1234", device.getDeviceUuid());
        assertEquals(8589934592L, device.getTotalMemory());
    }

    @Test
    public void uiConfigs_dialogAndToolbar() {
        DialogConfig dialog = new DialogConfig.Builder()
                .title("Alert")
                .message("Operation completed")
                .positiveButtonText("Confirm")
                .cancelable(false)
                .build();
        assertEquals("Alert", dialog.getTitle());
        assertEquals("Operation completed", dialog.getMessage());
        assertEquals("Confirm", dialog.getPositiveButtonText());
        assertFalse(dialog.isCancelable());

        ToolbarConfig.ActionItem action = new ToolbarConfig.ActionItem("share", "Share", "ic_share", true);
        ToolbarConfig toolbar = new ToolbarConfig.Builder()
                .title("Home")
                .subtitle("Contracts Demo")
                .backgroundColor("#1E88E5")
                .titleColor("#FFFFFF")
                .addAction(action)
                .build();
        assertEquals("Home", toolbar.getTitle());
        assertEquals("Contracts Demo", toolbar.getSubtitle());
        assertEquals("#1E88E5", toolbar.getBackgroundColor());
        assertEquals(1, toolbar.getActions().size());
        assertEquals("share", toolbar.getActions().get(0).getId());
    }

    @Test
    public void executionContextAndWebSourceType() {
        Map<String, Object> attrs = new HashMap<>();
        attrs.put("traceId", "trace-999");
        ExecutionContext ctx = new ExecutionContext("session-1", "corr-2", attrs);
        assertEquals("session-1", ctx.getSessionId());
        assertEquals("corr-2", ctx.getCorrelationId());
        assertEquals("trace-999", ctx.getAttribute("traceId"));

        assertEquals(3, WebSourceType.values().length);
    }
}
