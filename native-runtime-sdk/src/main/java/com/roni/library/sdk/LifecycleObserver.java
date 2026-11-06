package com.roni.library.sdk;

/**
 * Observer interface for reacting to NativeRuntime master lifecycle transitions.
 */
public interface LifecycleObserver {
    void onInitialized(NativeRuntime runtime);
    void onSessionStarted(RuntimeSession session);
    void onSessionPaused(RuntimeSession session);
    void onSessionResumed(RuntimeSession session);
    void onSessionStopped(RuntimeSession session);
    void onReleased();
    void onError(int errorCode, String errorMessage);
}
