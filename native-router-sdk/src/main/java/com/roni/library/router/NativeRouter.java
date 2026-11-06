package com.roni.library.router;

import com.roni.library.contracts.api.IApiCallback;
import com.roni.library.contracts.api.IApiHandler;
import com.roni.library.contracts.api.NativeApiError;
import com.roni.library.contracts.api.NativeApiRequest;
import com.roni.library.contracts.api.NativeApiResponse;
import com.roni.library.contracts.common.IDisposable;
import com.roni.library.contracts.common.RuntimeState;
import com.roni.library.logger.NativeLogger;
import com.roni.library.router.handler.EchoCommandHandler;
import com.roni.library.router.handler.SystemDiagnosticHandler;
import com.roni.library.router.interceptor.IRouteInterceptor;
import com.roni.library.router.matcher.IRouteMatcher;
import com.roni.library.router.matcher.PatternRouteMatcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Core routing and dispatching engine for the NativeRuntime ecosystem (Layer 2).
 * Handles URI pattern matching, interceptor pipelines, synchronous and asynchronous
 * command dispatching, and fallback handling.
 *
 * @author infoadmstudio2020-cyber
 */
public class NativeRouter implements IRouter, IDisposable {

    private static final String TAG = "NativeRouter";
    private static volatile NativeRouter sInstance;
    private static final Object INSTANCE_LOCK = new Object();

    private final AtomicReference<RuntimeState> runtimeState = new AtomicReference<>(RuntimeState.UNINITIALIZED);
    private final AtomicBoolean disposed = new AtomicBoolean(false);

    private final Map<String, IApiHandler> routes = new ConcurrentHashMap<>();
    private final List<IRouteInterceptor> interceptors = new CopyOnWriteArrayList<>();
    private final IRouteMatcher routeMatcher = new PatternRouteMatcher();
    private volatile IApiHandler fallbackHandler;
    private ExecutorService executorService;

    public NativeRouter() {
        initialize();
    }

    /**
     * Retrieves the global singleton instance of NativeRouter.
     */
    public static NativeRouter getInstance() {
        if (sInstance == null) {
            synchronized (INSTANCE_LOCK) {
                if (sInstance == null) {
                    sInstance = new NativeRouter();
                }
            }
        }
        return sInstance;
    }

    /**
     * Sets or overrides the global singleton instance (e.g. for testing).
     */
    public static void setInstance(NativeRouter router) {
        synchronized (INSTANCE_LOCK) {
            sInstance = router;
        }
    }

    /**
     * Initializes the router state machine, thread pools, and registers default handlers.
     */
    public synchronized void initialize() {
        if (runtimeState.get() == RuntimeState.READY) {
            return;
        }

        runtimeState.set(RuntimeState.INITIALIZING);
        disposed.set(false);

        if (executorService == null || executorService.isShutdown()) {
            executorService = Executors.newFixedThreadPool(
                    Math.max(2, Runtime.getRuntime().availableProcessors()),
                    r -> {
                        Thread thread = new Thread(r, "NativeRouter-Worker");
                        thread.setDaemon(true);
                        return thread;
                    }
            );
        }

        // Register default handlers
        registerHandler(new EchoCommandHandler());
        registerHandler(new SystemDiagnosticHandler());

        runtimeState.set(RuntimeState.READY);

        try {
            NativeLogger.getInstance().i(TAG, "NativeRouter initialized and ready.");
        } catch (Throwable ignored) {
        }
    }

    @Override
    public void registerRoute(String pathPattern, IApiHandler handler) {
        if (pathPattern == null || handler == null) {
            return;
        }
        routes.put(pathPattern.trim(), handler);
    }

    @Override
    public void registerHandler(IApiHandler handler) {
        if (handler == null) {
            return;
        }

        String path = handler.getPath();
        if (path != null && !path.trim().isEmpty()) {
            registerRoute(path.trim(), handler);
        }
    }

    @Override
    public void unregisterRoute(String pathPattern) {
        if (pathPattern != null) {
            routes.remove(pathPattern.trim());
        }
    }

    public void setFallbackHandler(IApiHandler fallbackHandler) {
        this.fallbackHandler = fallbackHandler;
    }

    public IApiHandler getFallbackHandler() {
        return fallbackHandler;
    }

    @Override
    public void addInterceptor(IRouteInterceptor interceptor) {
        if (interceptor != null && !interceptors.contains(interceptor)) {
            interceptors.add(interceptor);
        }
    }

    @Override
    public void removeInterceptor(IRouteInterceptor interceptor) {
        if (interceptor != null) {
            interceptors.remove(interceptor);
        }
    }

    public List<IRouteInterceptor> getInterceptors() {
        return Collections.unmodifiableList(interceptors);
    }

    public Map<String, IApiHandler> getRoutes() {
        return Collections.unmodifiableMap(routes);
    }

    @Override
    public RouteMatchResult match(String requestUri) {
        if (requestUri == null || requestUri.trim().isEmpty()) {
            return RouteMatchResult.notFound();
        }

        String uri = requestUri.trim();

        // Check registered routes
        for (Map.Entry<String, IApiHandler> entry : routes.entrySet()) {
            RouteMatchResult result = routeMatcher.match(entry.getKey(), uri, entry.getValue());
            if (result.isMatched()) {
                return result;
            }
        }

        // Check fallback handler
        if (fallbackHandler != null) {
            return RouteMatchResult.success("*", fallbackHandler, Collections.emptyMap(), Collections.emptyMap());
        }

        return RouteMatchResult.notFound();
    }

    @Override
    public boolean canHandle(String requestUri) {
        return match(requestUri).isMatched();
    }

    @Override
    public void dispatch(NativeApiRequest request, IApiCallback callback) {
        if (disposed.get() || runtimeState.get() != RuntimeState.READY) {
            NativeApiError error = new NativeApiError("ROUTER_NOT_READY", "NativeRouter is not in READY state.");
            if (callback != null) {
                callback.onError(error);
            }
            onError(error);
            return;
        }

        if (request == null) {
            NativeApiError error = new NativeApiError("INVALID_REQUEST", "Request cannot be null.");
            if (callback != null) {
                callback.onError(error);
            }
            onError(error);
            return;
        }

        executorService.execute(() -> executePipeline(request, callback));
    }

    @Override
    public NativeApiResponse dispatchSync(NativeApiRequest request) {
        if (disposed.get() || runtimeState.get() != RuntimeState.READY) {
            NativeApiError error = new NativeApiError("ROUTER_NOT_READY", "NativeRouter is not in READY state.");
            onError(error);
            return NativeApiResponse.error(request != null ? request.getRequestId() : "req_err", 503, error);
        }

        if (request == null) {
            NativeApiError error = new NativeApiError("INVALID_REQUEST", "Request cannot be null.");
            onError(error);
            return NativeApiResponse.error("req_null", 400, error);
        }

        final NativeApiResponse[] responseHolder = new NativeApiResponse[1];
        final NativeApiError[] errorHolder = new NativeApiError[1];
        final CountDownLatch latch = new CountDownLatch(1);

        executePipeline(request, new IApiCallback() {
            @Override
            public void onResponse(NativeApiResponse response) {
                responseHolder[0] = response;
                latch.countDown();
            }

            @Override
            public void onError(NativeApiError error) {
                errorHolder[0] = error;
                latch.countDown();
            }
        });

        try {
            boolean completed = latch.await(30, TimeUnit.SECONDS);
            if (!completed) {
                NativeApiError timeoutError = new NativeApiError("TIMEOUT", "Route dispatch timed out.");
                onError(timeoutError);
                return NativeApiResponse.error(request.getRequestId(), 408, timeoutError);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            NativeApiError error = new NativeApiError("INTERRUPTED", "Route dispatch interrupted: " + e.getMessage());
            onError(error);
            return NativeApiResponse.error(request.getRequestId(), 500, error);
        }

        if (responseHolder[0] != null) {
            return responseHolder[0];
        }

        NativeApiError error = errorHolder[0] != null ? errorHolder[0] : new NativeApiError("UNKNOWN_ERROR", "Unknown dispatch failure");
        return NativeApiResponse.error(request.getRequestId(), 500, error);
    }

    private void executePipeline(NativeApiRequest request, IApiCallback callback) {
        String uri = request.getPath();
        RouteMatchResult matchResult = match(uri);

        if (!matchResult.isMatched()) {
            NativeApiError notFoundError = new NativeApiError("404", "Route not found for URI: " + uri, uri);
            notifyInterceptorsError(request, notFoundError);
            if (callback != null) {
                callback.onError(notFoundError);
            }
            onError(notFoundError);
            return;
        }

        // Execute preHandle on all interceptors in order
        List<IRouteInterceptor> executedInterceptors = new ArrayList<>();
        for (IRouteInterceptor interceptor : interceptors) {
            try {
                boolean proceed = interceptor.preHandle(request, matchResult, callback);
                executedInterceptors.add(interceptor);
                if (!proceed) {
                    // Interceptor halted pipeline and handled response/error
                    return;
                }
            } catch (Throwable t) {
                NativeApiError interceptorError = new NativeApiError(
                        "INTERCEPTOR_ERROR",
                        "Error in interceptor " + interceptor.getClass().getSimpleName() + ": " + t.getMessage(),
                        t.toString()
                );
                notifyInterceptorsError(request, interceptorError);
                if (callback != null) {
                    callback.onError(interceptorError);
                }
                onError(interceptorError);
                return;
            }
        }

        // Execute Handler
        IApiHandler handler = matchResult.getHandler();
        try {
            NativeApiResponse response = handler.handleRequest(request);
            if (response == null) {
                response = NativeApiResponse.success(request.getRequestId());
            }

            // Post-handle in reverse order
            for (int i = executedInterceptors.size() - 1; i >= 0; i--) {
                try {
                    executedInterceptors.get(i).postHandle(request, response);
                } catch (Throwable ignored) {
                }
            }

            if (callback != null) {
                callback.onResponse(response);
            }
            onSuccess(response);
        } catch (Throwable t) {
            NativeApiError handlerError = new NativeApiError(
                    "HANDLER_EXECUTION_ERROR",
                    "Error executing handler " + handler.getClass().getSimpleName() + ": " + t.getMessage(),
                    t.toString()
            );
            notifyInterceptorsError(request, handlerError);
            if (callback != null) {
                callback.onError(handlerError);
            }
            onError(handlerError);
        }
    }

    private void notifyInterceptorsError(NativeApiRequest request, NativeApiError error) {
        for (IRouteInterceptor interceptor : interceptors) {
            try {
                interceptor.onError(request, error);
            } catch (Throwable ignored) {
            }
        }
    }

    /**
     * Telemetry hook called upon successful dispatch.
     */
    public void onSuccess(NativeApiResponse response) {
        // Can be overridden or extended for monitoring telemetry
    }

    /**
     * Telemetry hook called upon dispatch error.
     */
    public void onError(NativeApiError error) {
        // Can be overridden or extended for error logging
    }

    public boolean isInitialized() {
        return runtimeState.get() == RuntimeState.READY;
    }

    public RuntimeState getState() {
        return runtimeState.get();
    }

    /**
     * Releases all router resources and resets singleton state.
     */
    public synchronized void release() {
        dispose();
    }

    @Override
    public synchronized void dispose() {
        if (disposed.compareAndSet(false, true)) {
            runtimeState.set(RuntimeState.TERMINATED);
            routes.clear();
            interceptors.clear();
            fallbackHandler = null;

            if (executorService != null && !executorService.isShutdown()) {
                executorService.shutdown();
                try {
                    if (!executorService.awaitTermination(2, TimeUnit.SECONDS)) {
                        executorService.shutdownNow();
                    }
                } catch (InterruptedException e) {
                    executorService.shutdownNow();
                    Thread.currentThread().interrupt();
                }
                executorService = null;
            }

            synchronized (INSTANCE_LOCK) {
                if (sInstance == this) {
                    sInstance = null;
                }
            }
        }
    }

    @Override
    public boolean isDisposed() {
        return disposed.get();
    }
}
