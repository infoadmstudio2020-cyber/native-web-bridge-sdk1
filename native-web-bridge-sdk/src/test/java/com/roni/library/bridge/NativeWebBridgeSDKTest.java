package com.roni.library.bridge;

import com.roni.library.bridge.event.BridgeEvent;
import com.roni.library.bridge.event.BridgeEventBus;
import com.roni.library.bridge.js.JavaScriptBridgeScript;
import com.roni.library.bridge.message.BridgeMessage;
import com.roni.library.bridge.message.BridgeMessageType;
import com.roni.library.bridge.serializer.JsonMessageSerializer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

public class NativeWebBridgeSDKTest {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
        WebBridge.getInstance().release();
    }

    @Test
    public void testBridgeConfigBuilder() {
        BridgeConfig config = BridgeConfig.builder()
                .interfaceName("CustomBridge")
                .addAllowedOrigin("https://example.com")
                .timeoutMs(5000L)
                .debugLogging(true)
                .build();

        assertEquals("CustomBridge", config.getInterfaceName());
        assertTrue(config.isOriginAllowed("https://example.com"));
        assertFalse(config.isOriginAllowed("https://malicious.com"));
        assertEquals(5000L, config.getTimeoutMs());
        assertTrue(config.isDebugLogging());
    }

    @Test
    public void testBridgeConfigWildcardOrigin() {
        BridgeConfig config = BridgeConfig.builder()
                .addAllowedOrigin("*")
                .build();

        assertTrue(config.isOriginAllowed("https://any-domain.com"));
        assertTrue(config.isOriginAllowed("http://localhost:3000"));
    }

    @Test
    public void testBridgeMessageCreation() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("query", "test");

        BridgeMessage req = BridgeMessage.request("req_1", "search", payload);
        assertEquals("req_1", req.getId());
        assertEquals(BridgeMessageType.REQUEST, req.getType());
        assertEquals("search", req.getAction());
        assertEquals("test", req.getPayload().get("query"));
        assertTrue(req.isSuccess());

        BridgeMessage ping = BridgeMessage.ping("ping_1");
        assertEquals(BridgeMessageType.PING, ping.getType());
        assertTrue((Boolean) ping.getPayload().get("pong"));

        BridgeMessage err = BridgeMessage.error("req_2", "search", "Timeout error");
        assertFalse(err.isSuccess());
        assertEquals("Timeout error", err.getError());
    }

    @Test
    public void testJsonMessageSerializer() {
        JsonMessageSerializer serializer = new JsonMessageSerializer();

        Map<String, Object> payload = new HashMap<>();
        payload.put("number", 42);
        payload.put("text", "hello");

        BridgeMessage original = BridgeMessage.request("req_123", "echo", payload);
        String json = serializer.serialize(original);
        assertNotNull(json);
        assertTrue(json.contains("req_123"));
        assertTrue(json.contains("echo"));

        BridgeMessage deserialized = serializer.deserialize(json);
        assertEquals(original.getId(), deserialized.getId());
        assertEquals(original.getAction(), deserialized.getAction());
        assertEquals(original.getType(), deserialized.getType());
    }

    @Test
    public void testBridgeEventBusPublishSubscribe() {
        BridgeEventBus eventBus = new BridgeEventBus();
        AtomicReference<String> receivedTopic = new AtomicReference<>();
        AtomicBoolean wildcardTriggered = new AtomicBoolean(false);

        eventBus.subscribe("user_login", event -> receivedTopic.set(event.getTopic()));
        eventBus.subscribe("*", event -> wildcardTriggered.set(true));

        Map<String, Object> data = new HashMap<>();
        data.put("userId", "user_99");
        eventBus.publish(BridgeEvent.of("user_login", data));

        assertEquals("user_login", receivedTopic.get());
        assertTrue(wildcardTriggered.get());
    }

    @Test
    public void testBridgeSecurityValidator() {
        BridgeConfig config = BridgeConfig.builder()
                .addAllowedOrigin("https://trusted.com")
                .build();
        BridgeSecurityValidator validator = new BridgeSecurityValidator(config);

        assertTrue(validator.isOriginValid("https://trusted.com"));
        assertFalse(validator.isOriginValid("https://untrusted.com"));

        BridgeMessage validMsg = BridgeMessage.request("id_1", "action_1", null);
        assertTrue(validator.isMessageValid(validMsg));

        BridgeMessage invalidMsg = new BridgeMessage("", BridgeMessageType.REQUEST, "", null, null, true, 0);
        assertFalse(validator.isMessageValid(invalidMsg));
    }

    @Test
    public void testJavaScriptBridgeScriptInjection() {
        String script = JavaScriptBridgeScript.getInjectionScript("CyberBridge");
        assertNotNull(script);
        assertTrue(script.contains("CyberBridgeClient"));
        assertTrue(script.contains("CyberBridge"));
    }

    @Test
    public void testWebBridgeLifecycleAndMessageHandling() {
        WebBridge bridge = WebBridge.getInstance();
        assertNotNull(bridge);
        assertNotNull(bridge.getConfig());
        assertNotNull(bridge.getEventBus());
        assertNotNull(bridge.getSerializer());

        AtomicReference<String> evaluatedScript = new AtomicReference<>();
        bridge.setWebEvaluator(evaluatedScript::set);

        // Test ping message
        String pingJson = "{\"id\":\"p1\",\"type\":\"PING\",\"action\":\"ping\",\"payload\":{}}";
        bridge.handleWebMessage(pingJson);

        assertNotNull(evaluatedScript.get());
        assertTrue(evaluatedScript.get().contains("pong"));

        // Test WebBridge emitToWeb
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("status", "connected");
        bridge.emitToWeb("system_status", eventData);

        assertNotNull(evaluatedScript.get());
        assertTrue(evaluatedScript.get().contains("system_status"));
    }

    @Test
    public void testResultWrapper() {
        NativeWebBridgeSDKResult<String> res = NativeWebBridgeSDKResult.success("data");
        assertTrue(res.isSuccess());
        assertEquals("data", res.getData());

        NativeWebBridgeSDKResult<Void> err = NativeWebBridgeSDKResult.error("Failure", 500);
        assertFalse(err.isSuccess());
        assertEquals("Failure", err.getErrorMessage());
        assertEquals(500, err.getErrorCode());
    }
}
