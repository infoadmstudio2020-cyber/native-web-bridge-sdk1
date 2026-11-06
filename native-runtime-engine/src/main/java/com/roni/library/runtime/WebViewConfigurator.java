package com.roni.library.runtime;

import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Applies security, CSP, and performance configurations to WebView instances.
 */
public class WebViewConfigurator {

    private final NativeRuntimeEngineConfig config;

    public WebViewConfigurator(NativeRuntimeEngineConfig config) {
        this.config = config != null ? config : NativeRuntimeEngineConfig.defaultConfiguration();
    }

    public void configure(WebView webView) {
        if (webView == null) return;

        WebSettings settings = webView.getSettings();
        if (settings != null) {
            settings.setJavaScriptEnabled(true);
            settings.setDomStorageEnabled(true);
            settings.setDatabaseEnabled(true);

            if (config.isAllowMixedContent()) {
                settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            } else {
                settings.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);
            }

            settings.setAllowFileAccess(false);
            settings.setAllowContentAccess(false);
        }

        if (config.isHardwareAccelerationEnabled()) {
            webView.setLayerType(WebView.LAYER_TYPE_HARDWARE, null);
        } else {
            webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        }
    }

    public NativeRuntimeEngineConfig getConfig() {
        return config;
    }
}
