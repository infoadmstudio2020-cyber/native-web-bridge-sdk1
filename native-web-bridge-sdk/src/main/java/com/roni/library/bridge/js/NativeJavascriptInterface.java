package com.roni.library.bridge.js;

import android.webkit.JavascriptInterface;
import com.roni.library.bridge.WebBridge;

/**
 * Injected WebView Javascript Interface enabling Web to call Native methods.
 */
public final class NativeJavascriptInterface {

    private final WebBridge bridge;

    public NativeJavascriptInterface(WebBridge bridge) {
        this.bridge = bridge;
    }

    @JavascriptInterface
    public void postMessage(String jsonMessage) {
        if (bridge != null) {
            bridge.handleWebMessage(jsonMessage);
        }
    }

    @JavascriptInterface
    public String ping() {
        return "pong";
    }
}
