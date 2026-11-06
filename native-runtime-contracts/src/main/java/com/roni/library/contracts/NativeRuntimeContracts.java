package com.roni.library.contracts;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.roni.library.contracts.api.IApiHandler;
import com.roni.library.contracts.api.NativeApiError;
import com.roni.library.contracts.api.NativeApiRequest;
import com.roni.library.contracts.api.NativeApiResponse;
import com.roni.library.contracts.common.RuntimeState;
import com.roni.library.contracts.interfaces.INativeRuntimeContractsListener;
import com.roni.library.contracts.logger.ILogger;
import com.roni.library.contracts.logger.LogEntry;
import com.roni.library.contracts.logger.LogLevel;
import com.roni.library.contracts.service.IService;
import com.roni.library.contracts.service.IServiceRegistry;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * NativeRuntimeContracts — Primary Public Entry Point & Universal Foundation Engine (Layer 0).
 *
 * <p>Enforces a thread-safe singleton state machine:
 * <pre>
 *   UNINITIALIZED -> INITIALIZING -> READY -> SUSPENDED -> TERMINATED
 * </pre>
 *
 * <p>Usage:
 * <pre>
 *   NativeRuntimeContracts lib = NativeRuntimeContracts.getInstance();
 *   NativeRuntimeContractsConfig config = new NativeRuntimeContractsConfig.Builder()
 *       .debugEnabled(true)
 *       .build();
 *   lib.init(context, config);
 * </pre>
 *
 * @author  infoadmstudio2020-cyber
 * @version 1.0.2
 */
public final class NativeRuntimeContracts {

    private static volatile NativeRuntimeContracts sInstance;

    private final AtomicReference<RuntimeState> mState = new AtomicReference<>(RuntimeState.UNINITIALIZED);
    private final List<INativeRuntimeContractsListener> mListeners = new CopyOnWriteArrayList<>();
    private final Map<String, IApiHandler> mApiHandlers = new ConcurrentHashMap<>();
    private final Map<Class<? extends IService>, IService> mServiceRegistry = new ConcurrentHashMap<>();

    private Context mContext;
    private NativeRuntimeContractsConfig mConfig;
    private ILogger mCustomLogger;

    /** Private constructor — use {@link #getInstance()}. */
    private NativeRuntimeContracts() {}

    /**
     * Returns the thread-safe singleton instance of {@link NativeRuntimeContracts}.
     *
     * @return singleton instance
     */
    @NonNull
    public static NativeRuntimeContracts getInstance() {
        if (sInstance == null) {
            synchronized (NativeRuntimeContracts.class) {
                if (sInstance == null) {
                    sInstance = new NativeRuntimeContracts();
                }
            }
        }
        return sInstance;
    }

    /**
     * Initialize the library with application context and configuration.
     * Transitions state from UNINITIALIZED -> INITIALIZING -> READY.
     *
     * @param context application context (non-null)
     * @param config  library configuration (non-null)
     * @return NativeRuntimeContractsResult wrapping the active state
     */
    @NonNull
    public synchronized NativeRuntimeContractsResult<RuntimeState> init(@Nullable Context context,
                                                                        @NonNull NativeRuntimeContractsConfig config) {
        if (context == null) {
            NativeRuntimeContractsLogger.e(NativeRuntimeContractsConstants.TAG, "init failed: context cannot be null");
            notifyError(-101, "Context cannot be null");
            return NativeRuntimeContractsResult.error("Context cannot be null", -101);
        }

        RuntimeState current = mState.get();
        if (current == RuntimeState.READY) {
            NativeRuntimeContractsLogger.w(NativeRuntimeContractsConstants.TAG, "Already initialized and ready.");
            return NativeRuntimeContractsResult.success(current);
        }

        transitionState(RuntimeState.INITIALIZING);
        mContext = context.getApplicationContext() != null ? context.getApplicationContext() : context;
        mConfig = config;
        NativeRuntimeContractsLogger.setDebug(config.isDebugEnabled());

        transitionState(RuntimeState.READY);
        NativeRuntimeContractsLogger.i(NativeRuntimeContractsConstants.TAG,
                "NativeRuntimeContracts v" + NativeRuntimeContractsConstants.VERSION + " successfully initialized.");

        for (INativeRuntimeContractsListener listener : mListeners) {
            try {
                listener.onInitialized();
            } catch (Throwable t) {
                NativeRuntimeContractsLogger.e(NativeRuntimeContractsConstants.TAG, "Error notifying onInitialized", t);
            }
        }

        return NativeRuntimeContractsResult.success(RuntimeState.READY);
    }

    /**
     * Transition the state machine safely and notify registered listeners.
     */
    private boolean transitionState(@NonNull RuntimeState nextState) {
        RuntimeState previous = mState.getAndSet(nextState);
        if (previous != nextState) {
            NativeRuntimeContractsLogger.d(NativeRuntimeContractsConstants.TAG,
                    "State transition: " + previous + " -> " + nextState);
            for (INativeRuntimeContractsListener listener : mListeners) {
                try {
                    listener.onStateChanged(previous, nextState);
                } catch (Throwable t) {
                    NativeRuntimeContractsLogger.e(NativeRuntimeContractsConstants.TAG, "Error in onStateChanged listener", t);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Suspend the runtime (e.g. when app moves to background).
     */
    @NonNull
    public synchronized NativeRuntimeContractsResult<RuntimeState> suspend() {
        if (mState.get() != RuntimeState.READY) {
            return NativeRuntimeContractsResult.error("Cannot suspend from state: " + mState.get(), -102);
        }
        transitionState(RuntimeState.SUSPENDED);
        return NativeRuntimeContractsResult.success(RuntimeState.SUSPENDED);
    }

    /**
     * Resume the suspended runtime.
     */
    @NonNull
    public synchronized NativeRuntimeContractsResult<RuntimeState> resume() {
        if (mState.get() != RuntimeState.SUSPENDED) {
            return NativeRuntimeContractsResult.error("Cannot resume from state: " + mState.get(), -103);
        }
        transitionState(RuntimeState.READY);
        return NativeRuntimeContractsResult.success(RuntimeState.READY);
    }

    /**
     * Terminate the runtime completely.
     */
    @NonNull
    public synchronized NativeRuntimeContractsResult<RuntimeState> terminate() {
        transitionState(RuntimeState.TERMINATED);
        return NativeRuntimeContractsResult.success(RuntimeState.TERMINATED);
    }

    /**
     * Check if the library has been initialized and is operational (state is READY).
     *
     * @return true if initialized and ready
     */
    public boolean isInitialized() {
        return mState.get() == RuntimeState.READY;
    }

    /**
     * Get the current lifecycle state of the runtime contracts engine.
     *
     * @return active RuntimeState
     */
    @NonNull
    public RuntimeState getState() {
        return mState.get();
    }

    /**
     * Return the current configuration.
     *
     * @return config, or null if not configured
     */
    @Nullable
    public NativeRuntimeContractsConfig getConfig() {
        return mConfig;
    }

    /**
     * Return application context or null if not initialized.
     */
    @Nullable
    public Context getContext() {
        return mContext;
    }

    /**
     * Register a listener for lifecycle and error events.
     *
     * @param listener listener callback
     */
    public void addListener(@NonNull INativeRuntimeContractsListener listener) {
        if (!mListeners.contains(listener)) {
            mListeners.add(listener);
        }
    }

    /**
     * Unregister a listener.
     *
     * @param listener listener callback
     */
    public void removeListener(@NonNull INativeRuntimeContractsListener listener) {
        mListeners.remove(listener);
    }

    /**
     * Register an API Command Handler for RPC dispatching.
     *
     * @param handler handler instance
     */
    public void registerApiHandler(@NonNull IApiHandler handler) {
        String key = handler.getPath() + "/" + handler.getAction();
        mApiHandlers.put(key, handler);
    }

    /**
     * Dispatch an API Request through registered handlers.
     *
     * @param request the incoming request
     * @return standardized response
     */
    @NonNull
    public NativeApiResponse dispatchApiRequest(@NonNull NativeApiRequest request) {
        String exactKey = request.getPath() + "/" + request.getAction();
        IApiHandler handler = mApiHandlers.get(exactKey);
        if (handler == null) {
            String wildcardKey = request.getPath() + "/*";
            handler = mApiHandlers.get(wildcardKey);
        }

        if (handler != null) {
            try {
                return handler.handleRequest(request);
            } catch (Throwable t) {
                return NativeApiResponse.error(request.getRequestId(), 500,
                        new NativeApiError("HANDLER_EXCEPTION", t.getMessage(), null));
            }
        }
        return NativeApiResponse.error(request.getRequestId(), 404,
                new NativeApiError("NOT_FOUND", "No handler registered for path: " + request.getPath(), null));
    }

    /**
     * Register a service into the runtime service registry.
     */
    public <T extends IService> void registerService(@NonNull Class<T> serviceClass, @NonNull T instance) {
        mServiceRegistry.put(serviceClass, instance);
    }

    /**
     * Retrieve a service from the registry.
     */
    @Nullable
    @SuppressWarnings("unchecked")
    public <T extends IService> T getService(@NonNull Class<T> serviceClass) {
        return (T) mServiceRegistry.get(serviceClass);
    }

    /**
     * Set a custom logging sink.
     */
    public void setCustomLogger(@Nullable ILogger logger) {
        this.mCustomLogger = logger;
    }

    /**
     * Log an event via internal or custom logger.
     */
    public void log(@NonNull LogLevel level, @NonNull String tag, @NonNull String message) {
        if (mCustomLogger != null) {
            mCustomLogger.log(new LogEntry(level, tag, message));
        } else {
            switch (level) {
                case DEBUG: NativeRuntimeContractsLogger.d(tag, message); break;
                case WARN:  NativeRuntimeContractsLogger.w(tag, message); break;
                case ERROR: NativeRuntimeContractsLogger.e(tag, message); break;
                default:    NativeRuntimeContractsLogger.i(tag, message); break;
            }
        }
    }

    /**
     * Helper to encode byte array into standard Base64 string.
     */
    @NonNull
    public static String encodeBase64(@NonNull byte[] data) {
        return java.util.Base64.getEncoder().encodeToString(data);
    }

    /**
     * Helper to decode Base64 string into byte array.
     */
    @NonNull
    public static byte[] decodeBase64(@NonNull String base64) {
        return java.util.Base64.getDecoder().decode(base64);
    }

    private void notifyError(int code, @NonNull String message) {
        for (INativeRuntimeContractsListener listener : mListeners) {
            try {
                listener.onError(code, message);
            } catch (Throwable ignored) {}
        }
    }

    /** Release all resources held by this instance and reset state. */
    public synchronized void release() {
        mState.set(RuntimeState.TERMINATED);
        mListeners.clear();
        mApiHandlers.clear();
        mServiceRegistry.clear();
        mConfig = null;
        mContext = null;
        mCustomLogger = null;
        sInstance = null;
    }
}

