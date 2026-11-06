package com.roni.library.contracts.runtime;

import androidx.annotation.NonNull;

/**
 * Universal descriptor for hybrid web source bundle loaded into WebView or Server.
 *
 * @author infoadmstudio2020-cyber
 */
public interface IWebSource {

    /**
     * Get the resolved URL or entry URI of the web source.
     *
     * @return URL or path string
     */
    @NonNull
    String getUrl();

    /**
     * Get the source storage and resolution type.
     *
     * @return web source type
     */
    @NonNull
    WebSourceType getType();

    /**
     * Check if the web content assets or server are ready and accessible.
     *
     * @return true if available
     */
    boolean isAvailable();
}
