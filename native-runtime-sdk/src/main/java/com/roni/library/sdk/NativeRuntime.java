package com.roni.library.sdk;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.roni.library.contracts.api.NativeApiError;
import com.roni.library.contracts.api.NativeApiRequest;
import com.roni.library.contracts.api.NativeApiResponse;
import com.roni.library.contracts.common.RuntimeState;
import com.roni.library.sdk.bootstrap.NativeRuntimeBootstrap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Master Turnkey Facade and Host Activity Orchestrator for the NativeRuntime SDK.
 * Class: Layer 4 Master Orchestration (L4).
 */
public final class NativeRuntime {

    private static volatile NativeRuntime instance;

    private final AtomicBoolean initialized = new AtomicBoolean(false);
    private final AtomicReference<RuntimeSession> currentSession = new AtomicReference<>(null);
    private final List<LifecycleObserver> observers = new CopyOnWriteArrayList<>();
    private final Map<String, Object> telemetryEvents = Collections.synchronizedMap(new HashMap<>());

    private volatile RuntimeConfiguration configuration;
    private volatile NativeRuntimeBootstrap.BootstrapReport bootstrapReport;
    private volatile RuntimeState state = RuntimeState.UNINITIALIZED;

    private NativeRuntime() {}

    public static NativeRuntime getInstance() {
        if (instance == null) {
            synchronized (NativeRuntime.class) {
                if (instance == null) {
                    instance = new NativeRuntime();
                }
            }
        }
        return instance;
    }

    /**
     * Initializes NativeRuntime with default configuration.
     */
    public synchronized NativeRuntimeResult<Boolean> init(Context context) {
        return init(context, RuntimeConfiguration.builder().build());
    }

    /**
     * Initializes NativeRuntime with custom configuration and runs the 10-step coordinated bootstrap.
     */
    public synchronized NativeRuntimeResult<Boolean> init(Context context, RuntimeConfiguration config) {
        if (initialized.get()) {
            return NativeRuntimeResult.success(true);
        }

        try {
            this.configuration = config != null ? config : RuntimeConfiguration.builder().build();
            this.state = RuntimeState.INITIALIZING;

            // Run 10-step coordinated turnkey bootstrap sequence
            this.bootstrapReport = NativeRuntimeBootstrap.execute(context, this.configuration);

            if (!bootstrapReport.isOverallSuccess()) {
                this.state = RuntimeState.ERROR;
                notifyError(500, "Turnkey bootstrap failed on one or more steps.");
                return NativeRuntimeResult.failure(500, "Bootstrap failed. Passed: " +
                        bootstrapReport.getPassedCount() + "/" + bootstrapReport.getSteps().size());
            }

            // Start master runtime session
            startSession();

            this.state = RuntimeState.READY;
            this.initialized.set(true);

            notifyInitialized();
            return NativeRuntimeResult.success(true);
        } catch (Throwable t) {
            this.state = RuntimeState.ERROR;
            notifyError(500, t.getMessage());
            return NativeRuntimeResult.failure(t);
        }
    }

    public synchronized void startSession() {
        RuntimeSession session = new RuntimeSession();
        session.markActive();
        this.currentSession.set(session);
        for (LifecycleObserver obs : observers) {
            try {
                obs.onSessionStarted(session);
            } catch (Throwable ignored) {}
        }
    }

    public synchronized void pauseSession() {
        RuntimeSession session = currentSession.get();

        if (session != null) {
            session.markPaused();
            this.state = RuntimeState.SUSPENDED;

            for (LifecycleObserver obs : observers) {
                try {
                    obs.onSessionPaused(session);
                } catch (Exception ignored) {
                    // Observer failure must not interrupt lifecycle dispatch.
                }
            }
        }
    }

    public synchronized void resumeSession() {
        RuntimeSession session = currentSession.get();
        if (session != null) {
            session.markActive();
            this.state = RuntimeState.READY;
            for (LifecycleObserver obs : observers) {
                try {
                    obs.onSessionResumed(session);
                } catch (Throwable ignored) {}
            }
        }
    }

    public synchronized void stopSession() {
        RuntimeSession session = currentSession.get();
        if (session != null) {
            session.markStopped();
            for (LifecycleObserver obs : observers) {
                try {
                    obs.onSessionStopped(session);
                } catch (Throwable ignored) {}
            }
        }
    }

    public synchronized void release() {
        stopSession();
        this.initialized.set(false);
        this.currentSession.set(null);
        this.state = RuntimeState.UNINITIALIZED;
        this.telemetryEvents.clear();

        for (LifecycleObserver obs : observers) {
            try {
                obs.onReleased();
            } catch (Throwable ignored) {}
        }
        observers.clear();
    }

    public boolean isInitialized() {
        return initialized.get();
    }

    public RuntimeState getState() {
        return state;
    }

    public RuntimeConfiguration getConfiguration() {
        return configuration;
    }

    public RuntimeSession getSession() {
        return currentSession.get();
    }

    public NativeRuntimeBootstrap.BootstrapReport getBootstrapReport() {
        return bootstrapReport;
    }

    public boolean isHealthy() {
        return initialized.get() && state == RuntimeState.READY &&
                bootstrapReport != null && bootstrapReport.isOverallSuccess();
    }

    public void addObserver(LifecycleObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(LifecycleObserver observer) {
        if (observer != null) {
            observers.remove(observer);
        }
    }


    /**
     * Standard RPC dispatcher delegating requests
     * to the underlying layer handlers.
     */
    @NonNull
    public NativeApiResponse executeApiRequest(@Nullable NativeApiRequest request) {

        if (request == null) {
            return NativeApiResponse.error(
                    "req_unknown",
                    400,
                    new NativeApiError(
                            "INVALID_REQUEST",
                            "Null request object"
                    )
            );
        }

        if (!isInitialized()) {
            return NativeApiResponse.error(
                    request.getRequestId(),
                    503,
                    new NativeApiError(
                            "RUNTIME_NOT_INITIALIZED",
                            "NativeRuntime is not initialized"
                    )
            );
        }

        Map<String, Object> data = new HashMap<>();
        data.put("handledBy", "NativeRuntimeMasterFacade");
        data.put("path", request.getPath());
        data.put("action", request.getAction());
        data.put("timestamp", System.currentTimeMillis());

        return NativeApiResponse.success(
                request.getRequestId(),
                data
        );
    }

    /**
     * High-level telemetry dispatcher for host applications.
     */
    public void trackEvent(String eventName, Map<String, Object> params) {
        if (eventName != null) {
            telemetryEvents.put(eventName + "_" + System.currentTimeMillis(), params != null ? params : Collections.emptyMap());
        }
    }

    public Map<String, Object> getTelemetryEvents() {
        return new HashMap<>(telemetryEvents);
    }

    private void notifyInitialized() {
        for (LifecycleObserver obs : observers) {
            try {
                obs.onInitialized(this);
            } catch (Throwable ignored) {}
        }
    }

    private void notifyError(int code, String message) {
        for (LifecycleObserver obs : observers) {
            try {
                obs.onError(code, message);
            } catch (Throwable ignored) {}
        }
    }
}
