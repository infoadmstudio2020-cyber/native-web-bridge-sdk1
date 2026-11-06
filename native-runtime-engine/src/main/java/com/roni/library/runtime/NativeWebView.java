package com.roni.library.runtime;

import android.content.Context;
import com.roni.library.runtime.container.WebViewContainer;
import com.roni.library.runtime.source.WebSource;

/**
 * High-performance, hardware-accelerated Native WebView abstraction.
 */
public class NativeWebView {

    private final WebViewContainer container;
    private final WebResourceInterceptor interceptor;
    private final WebViewConfigurator configurator;

    public NativeWebView(Context context, NativeRuntimeEngineConfig config) {
        this.container = new WebViewContainer("webview-" + System.currentTimeMillis());
        this.configurator = new WebViewConfigurator(config);
        this.interceptor = new WebResourceInterceptor(null);
    }

    public WebViewContainer getContainer() {
        return container;
    }

    public WebResourceInterceptor getInterceptor() {
        return interceptor;
    }

    public WebViewConfigurator getConfigurator() {
        return configurator;
    }

    public void loadUrl(String url) {
        WebSource source = interceptor.intercept(url);
        if (source != null && source.isFound()) {
            container.loadUrl(url);
        } else {
            container.loadUrl(url != null ? url : "about:blank");
        }
    }

    public void destroy() {
        container.destroy();
    }
}
