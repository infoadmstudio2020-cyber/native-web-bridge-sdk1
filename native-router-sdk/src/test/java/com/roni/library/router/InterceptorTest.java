package com.roni.library.router;

import com.roni.library.contracts.api.IApiCallback;
import com.roni.library.contracts.api.NativeApiError;
import com.roni.library.contracts.api.NativeApiRequest;
import com.roni.library.contracts.api.NativeApiResponse;
import com.roni.library.router.interceptor.AuthInterceptor;
import com.roni.library.router.interceptor.LoggingInterceptor;
import com.roni.library.router.interceptor.PerformanceInterceptor;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

/**
 * Unit tests for Route Interceptors (Auth, Logging, Performance).
 *
 * @author infoadmstudio2020-cyber
 */
public class InterceptorTest {

    @Test
    public void testAuthInterceptorPermitsUnprotected() {
        AuthInterceptor interceptor = new AuthInterceptor("secret123");
        interceptor.addProtectedPrefix("/admin");

        NativeApiRequest request = new NativeApiRequest("1", "/public/home", "get", new HashMap<>(), System.currentTimeMillis());
        boolean allowed = interceptor.preHandle(request, RouteMatchResult.notFound(), null);
        assertTrue(allowed);
    }

    @Test
    public void testAuthInterceptorRejectsWithoutToken() {
        AuthInterceptor interceptor = new AuthInterceptor("secret123");
        interceptor.addProtectedPrefix("/admin");

        NativeApiRequest request = new NativeApiRequest("2", "/admin/settings", "get", new HashMap<>(), System.currentTimeMillis());
        AtomicBoolean errorReported = new AtomicBoolean(false);

        boolean allowed = interceptor.preHandle(request, RouteMatchResult.notFound(), new IApiCallback() {
            @Override
            public void onResponse(NativeApiResponse response) {}

            @Override
            public void onError(NativeApiError error) {
                errorReported.set(true);
                assertEquals("AUTH_401", error.getCode());
            }
        });

        assertFalse(allowed);
        assertTrue(errorReported.get());
    }

    @Test
    public void testAuthInterceptorAcceptsValidToken() {
        AuthInterceptor interceptor = new AuthInterceptor("secret123");
        interceptor.addProtectedPrefix("/admin");

        Map<String, Object> params = new HashMap<>();
        params.put("token", "secret123");
        NativeApiRequest request = new NativeApiRequest("3", "/admin/settings", "get", params, System.currentTimeMillis());

        boolean allowed = interceptor.preHandle(request, RouteMatchResult.notFound(), null);
        assertTrue(allowed);
    }

    @Test
    public void testLoggingInterceptorCounts() {
        LoggingInterceptor logging = new LoggingInterceptor();
        assertEquals(0, logging.getRequestCount());

        NativeApiRequest request = new NativeApiRequest("4", "/echo", "echo", new HashMap<>(), System.currentTimeMillis());
        logging.preHandle(request, RouteMatchResult.notFound(), null);
        assertEquals(1, logging.getRequestCount());
    }

    @Test
    public void testPerformanceInterceptorTracking() throws InterruptedException {
        PerformanceInterceptor perf = new PerformanceInterceptor();
        NativeApiRequest request = new NativeApiRequest("5", "/echo", "echo", new HashMap<>(), System.currentTimeMillis());

        perf.preHandle(request, RouteMatchResult.notFound(), null);
        Thread.sleep(10);
        perf.postHandle(request, NativeApiResponse.success("5"));

        assertTrue(perf.getLastExecutionDurationMs() >= 0);
        assertEquals(1, perf.getCompletedRequestCount());
    }
}
