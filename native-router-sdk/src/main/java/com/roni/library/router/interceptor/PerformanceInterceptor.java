package com.roni.library.router.interceptor;

import com.roni.library.contracts.api.IApiCallback;
import com.roni.library.contracts.api.NativeApiError;
import com.roni.library.contracts.api.NativeApiRequest;
import com.roni.library.contracts.api.NativeApiResponse;
import com.roni.library.router.RouteMatchResult;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Performance telemetry interceptor measuring route execution latencies.
 *
 * @author infoadmstudio2020-cyber
 */
public class PerformanceInterceptor implements IRouteInterceptor {

    private final ConcurrentHashMap<String, Long> startTimestamps = new ConcurrentHashMap<>();
    private final AtomicLong lastExecutionDurationMs = new AtomicLong(0);
    private final AtomicLong totalExecutionTimeMs = new AtomicLong(0);
    private final AtomicLong completedRequests = new AtomicLong(0);

    @Override
    public boolean preHandle(NativeApiRequest request, RouteMatchResult matchResult, IApiCallback callback) {
        if (request != null && request.getRequestId() != null) {
            startTimestamps.put(request.getRequestId(), System.currentTimeMillis());
        }
        return true;
    }

    @Override
    public void postHandle(NativeApiRequest request, NativeApiResponse response) {
        recordDuration(request);
    }

    @Override
    public void onError(NativeApiRequest request, NativeApiError error) {
        recordDuration(request);
    }

    private void recordDuration(NativeApiRequest request) {
        if (request != null && request.getRequestId() != null) {
            Long startTime = startTimestamps.remove(request.getRequestId());
            if (startTime != null) {
                long duration = Math.max(0, System.currentTimeMillis() - startTime);
                lastExecutionDurationMs.set(duration);
                totalExecutionTimeMs.addAndGet(duration);
                completedRequests.incrementAndGet();
            }
        }
    }

    public long getLastExecutionDurationMs() {
        return lastExecutionDurationMs.get();
    }

    public double getAverageExecutionDurationMs() {
        long count = completedRequests.get();
        if (count == 0) return 0.0;
        return (double) totalExecutionTimeMs.get() / count;
    }

    public long getCompletedRequestCount() {
        return completedRequests.get();
    }
}
