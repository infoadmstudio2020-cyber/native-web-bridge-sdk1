package com.roni.library.bridge;

import com.roni.library.bridge.event.BridgeEvent;
import com.roni.library.bridge.event.BridgeEventBus;
import com.roni.library.bridge.event.IEventBus;
import com.roni.library.bridge.handler.BridgeCommandHandler;
import com.roni.library.bridge.js.JavaScriptBridgeScript;
import com.roni.library.bridge.js.NativeJavascriptInterface;
import com.roni.library.bridge.message.BridgeMessage;
import com.roni.library.bridge.message.BridgeMessageType;
import com.roni.library.bridge.serializer.IMessageSerializer;
import com.roni.library.bridge.serializer.JsonMessageSerializer;
import com.roni.library.contracts.api.IApiCallback;
import com.roni.library.contracts.api.NativeApiError;
import com.roni.library.contracts.api.NativeApiRequest;
import com.roni.library.contracts.api.NativeApiResponse;
import com.roni.library.logger.NativeLogger;
import com.roni.library.router.IRouter;
import com.roni.library.router.NativeRouter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Primary Layer 3 Transport & Bridge Orchestrator.
 */
public final class WebBridge {

    private static final String TAG = "NativeWebBridgeSDK";
    private static volatile WebBridge sInstance;

    private final BridgeConfig config;
    private final IRouter router;
    private final IEventBus eventBus;
    private final IMessageSerializer serializer;
    private final NativeJavascriptInterface javascriptInterface;
    private final BridgeSecurityValidator securityValidator;
    private final BridgeCommandHandler commandHandler;

    private volatile java.util.function.Consumer<String> webEvaluator;
    private final AtomicBoolean initialized = new AtomicBoolean(false);
    private final ConcurrentHashMap<String, String> pendingRequests = new ConcurrentHashMap<>();

    private WebBridge(BridgeConfig config) {
        this.config = config != null ? config : BridgeConfig.defaultConfig();
        this.router = NativeRouter.getInstance();
        this.eventBus = new BridgeEventBus();
        this.serializer = new JsonMessageSerializer();
        this.javascriptInterface = new NativeJavascriptInterface(this);
        this.securityValidator = new BridgeSecurityValidator(this.config);
        this.commandHandler = new BridgeCommandHandler(this);

        if (this.router != null) {
            try {
                this.router.registerHandler(this.commandHandler);
                this.router.registerRoute("/bridge", this.commandHandler);
                this.router.registerRoute("/bridge/dispatch", this.commandHandler);
            } catch (Throwable t) {
                logWarn("Failed to register bridge route with router: " + t.getMessage());
            }
        }
        initialized.set(true);
        logInfo("WebBridge initialized with interface: " + this.config.getInterfaceName());
    }

    public static WebBridge getInstance() {
        if (sInstance == null) {
            synchronized (WebBridge.class) {
                if (sInstance == null) {
                    sInstance = new WebBridge(BridgeConfig.defaultConfig());
                }
            }
        }
        return sInstance;
    }

    public static synchronized WebBridge init(BridgeConfig config) {
        if (sInstance == null) {
            sInstance = new WebBridge(config);
        }
        return sInstance;
    }

    public BridgeConfig getConfig() {
        return config;
    }

    public IRouter getRouter() {
        return router;
    }

    public IEventBus getEventBus() {
        return eventBus;
    }

    public IMessageSerializer getSerializer() {
        return serializer;
    }

    public NativeJavascriptInterface getJavascriptInterface() {
        return javascriptInterface;
    }

    public void setWebEvaluator(java.util.function.Consumer<String> evaluator) {
        this.webEvaluator = evaluator;
    }

    public String getBootstrapScript() {
        return JavaScriptBridgeScript.getInjectionScript(config.getInterfaceName());
    }

    public void emitToWeb(String topic, Map<String, Object> data) {
        BridgeMessage eventMsg = BridgeMessage.event(topic, data);
        String rawJson = serializer.serialize(eventMsg);
        dispatchJs(rawJson);
    }

    public void handleWebMessage(String rawJson) {
        if (rawJson == null || rawJson.trim().isEmpty()) {
            return;
        }
        logDebug("Received web message: " + rawJson);

        BridgeMessage message = serializer.deserialize(rawJson);
        if (!securityValidator.isMessageValid(message)) {
            logWarn("Invalid message payload or missing action: " + rawJson);
            BridgeMessage errorMsg = BridgeMessage.error(message.getId(), message.getAction(), "Security validation failed");
            dispatchJs(serializer.serialize(errorMsg));
            return;
        }

        if (message.getType() == BridgeMessageType.PING) {
            BridgeMessage pong = BridgeMessage.ping(message.getId());
            dispatchJs(serializer.serialize(pong));
            return;
        }

        if (message.getType() == BridgeMessageType.EVENT) {
            eventBus.publish(BridgeEvent.of(message.getAction(), message.getPayload(), "web"));
            return;
        }

        // REQUEST handling
        String action = message.getAction();
        String path = action.startsWith("/") ? action : "/" + action;
        pendingRequests.put(message.getId(), action);

        NativeApiRequest request = new NativeApiRequest(
                message.getId(),
                path,
                action,
                message.getPayload(),
                message.getTimestamp()
        );

        if (router != null && router.canHandle(path)) {
            router.dispatch(request, new IApiCallback() {
                @Override
                public void onResponse(NativeApiResponse response) {
                    onSuccess(response);
                }

                @Override
                public void onError(NativeApiError error) {
                    WebBridge.this.onError(message.getId(), error);
                }
            });
        } else {
            // Fallback default dispatch response
            logDebug("Route not found in NativeRouter, creating standard response for: " + path);
            Map<String, Object> data = message.getPayload();
            NativeApiResponse fallback = NativeApiResponse.success(message.getId(), data);
            onSuccess(fallback);
        }
    }

    public void onSuccess(NativeApiResponse response) {
        if (response == null) {
            return;
        }
        String reqId = response.getRequestId();
        String action = pendingRequests.remove(reqId);
        BridgeMessage msg = BridgeMessage.response(reqId, action != null ? action : "", response.getData());
        dispatchJs(serializer.serialize(msg));
    }

    public void onError(NativeApiError error) {
        onError("unknown", error);
    }

    public void onError(String reqId, NativeApiError error) {
        String action = pendingRequests.remove(reqId);
        String errMsg = error != null ? error.getMessage() : "Unknown native error";
        BridgeMessage msg = BridgeMessage.error(reqId, action != null ? action : "", errMsg);
        dispatchJs(serializer.serialize(msg));
    }

    private void dispatchJs(String jsonString) {
        java.util.function.Consumer<String> evaluator = this.webEvaluator;
        if (evaluator != null) {
            String script = "if (window['" + config.getInterfaceName() + "Client'] && window['" +
                    config.getInterfaceName() + "Client']._handleNativeMessage) { window['" +
                    config.getInterfaceName() + "Client']._handleNativeMessage(" + jsonString + "); }";
            try {
                evaluator.accept(script);
            } catch (Throwable t) {
                logWarn("Failed to execute JS in webEvaluator: " + t.getMessage());
            }
        }
    }

    private void logDebug(String msg) {
        if (config.isDebugLogging()) {
            try {
                NativeLogger.getInstance().d(TAG, msg);
            } catch (Throwable ignored) {
            }
        }
    }

    private void logInfo(String msg) {
        try {
            NativeLogger.getInstance().i(TAG, msg);
        } catch (Throwable ignored) {
        }
    }

    private void logWarn(String msg) {
        try {
            NativeLogger.getInstance().w(TAG, msg);
        } catch (Throwable ignored) {
        }
    }

    public void release() {
        eventBus.clear();
        pendingRequests.clear();
        webEvaluator = null;
        initialized.set(false);
        sInstance = null;
    }
}
