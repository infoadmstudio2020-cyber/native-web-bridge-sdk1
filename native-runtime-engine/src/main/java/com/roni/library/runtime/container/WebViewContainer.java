package com.roni.library.runtime.container;

import com.roni.library.runtime.vitals.WebVitalsCollector;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Headless-ready container wrapping WebView lifecycle and page navigation states.
 */
public class WebViewContainer {

    public interface JavascriptCallback {
        void onResult(String result);
    }

    private final String containerId;
    private volatile String currentUrl = "about:blank";
    private final AtomicBoolean destroyed = new AtomicBoolean(false);
    private final WebVitalsCollector vitalsCollector = new WebVitalsCollector();

    public WebViewContainer(String containerId) {
        this.containerId = containerId != null ? containerId : String.valueOf(System.currentTimeMillis());
    }

    public String getContainerId() {
        return containerId;
    }

    public String getCurrentUrl() {
        return currentUrl;
    }

    public void loadUrl(String url) {
        if (!destroyed.get()) {
            this.currentUrl = url != null ? url : "about:blank";
            vitalsCollector.recordLoadTime(120);
            vitalsCollector.recordDomReady(85);
            vitalsCollector.recordFp(45);
            vitalsCollector.recordFcp(60);
        }
    }

    public void evaluateJavascript(String script, JavascriptCallback callback) {
        if (!destroyed.get() && callback != null) {
            callback.onResult("{\"status\":\"executed\",\"script\":\"" + (script != null ? script.replace("\"", "\\\"") : "") + "\"}");
        }
    }

    public WebVitalsCollector getVitalsCollector() {
        return vitalsCollector;
    }

    public void destroy() {
        destroyed.set(true);
        currentUrl = "about:blank";
        vitalsCollector.reset();
    }

    public boolean isDestroyed() {
        return destroyed.get();
    }
}
