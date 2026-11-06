package com.roni.library.contracts.runtime;

/**
 * Types of web content sources hosted by NativeRuntime.
 *
 * @author infoadmstudio2020-cyber
 */
public enum WebSourceType {

    /** Loaded directly from Android APK assets (file:///android_asset/...). */
    LOCAL_ASSET,

    /** Served via embedded lightweight localhost web server (http://127.0.0.1:port/...). */
    LOCAL_SERVER,

    /** Loaded from remote internet HTTPS URL. */
    REMOTE_URL
}
