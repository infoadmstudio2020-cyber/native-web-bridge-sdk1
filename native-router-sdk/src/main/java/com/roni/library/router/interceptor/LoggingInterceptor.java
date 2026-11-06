package com.roni.library.router.interceptor;

import com.roni.library.contracts.api.IApiCallback;
import com.roni.library.contracts.api.NativeApiError;
import com.roni.library.contracts.api.NativeApiRequest;
import com.roni.library.contracts.api.NativeApiResponse;
import com.roni.library.logger.NativeLogger;
import com.roni.library.router.RouteMatchResult;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Diagnostic interceptor that logs routing events using {@link NativeLogger} (Layer 1).
 *
 * @author infoadmstudio2020-cyber
 */
public class LoggingInterceptor implements IRouteInterceptor {

    private static final String TAG = "NativeRouter:Logging";
    private final AtomicLong requestCount = new AtomicLong(0);

    @Override
    public boolean preHandle(NativeApiRequest request, RouteMatchResult matchResult, IApiCallback callback) {
        long count = requestCount.incrementAndGet();
        String path = request != null ? request.getPath() : "null";
        String action = request != null ? request.getAction() : "null";
        String pattern = matchResult != null ? matchResult.getMatchedPattern() : "unknown";

        try {
            NativeLogger.getInstance().d(TAG, "==> Dispatching [# " + count + "] path=" + path +
                    ", action=" + action + ", matchedPattern=" + pattern);
        } catch (Throwable ignored) {
            // Fallback safety
        }
        return true;
    }

    @Override
    public void postHandle(NativeApiRequest request, NativeApiResponse response) {
        String path = request != null ? request.getPath() : "null";
        boolean success = response != null && response.isSuccess();
        int status = response != null ? response.getStatusCode() : -1;

        try {
            NativeLogger.getInstance().i(TAG, "<== Dispatched SUCCESS path=" + path +
                    ", status=" + status + ", success=" + success);
        } catch (Throwable ignored) {
        }
    }

    @Override
    public void onError(NativeApiRequest request, NativeApiError error) {
        String path = request != null ? request.getPath() : "null";
        String code = error != null ? error.getCode() : "UNKNOWN";
        String message = error != null ? error.getMessage() : "Unknown error";

        try {
            NativeLogger.getInstance().e(TAG, "<== Dispatch ERROR path=" + path +
                    ", code=" + code + ", msg=" + message);
        } catch (Throwable ignored) {
        }
    }

    public long getRequestCount() {
        return requestCount.get();
    }
}
