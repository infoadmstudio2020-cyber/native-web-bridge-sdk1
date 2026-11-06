package com.roni.library.router;

import com.roni.library.contracts.api.IApiCallback;
import com.roni.library.contracts.api.IApiHandler;
import com.roni.library.contracts.api.NativeApiError;
import com.roni.library.contracts.api.NativeApiRequest;
import com.roni.library.contracts.api.NativeApiResponse;
import com.roni.library.router.handler.EchoCommandHandler;
import com.roni.library.router.handler.SystemDiagnosticHandler;
import com.roni.library.router.interceptor.IRouteInterceptor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

/**
 * Comprehensive Unit Tests for {@link NativeRouter} core engine capabilities.
 *
 * @author infoadmstudio2020-cyber
 */
public class NativeRouterTest {

    private NativeRouter router;

    @Before
    public void setUp() {
        router = new NativeRouter();
        router.initialize();
    }

    @After
    public void tearDown() {
        if (router != null) {
            router.release();
        }
    }

    @Test
    public void testDefaultHandlersRegistered() {
        assertTrue(router.canHandle("/echo"));
        assertTrue(router.canHandle("/system/diagnostic"));
    }

    @Test
    public void testRegisterAndUnregisterRoute() {
        String testPath = "/api/v1/test";
        IApiHandler handler = new IApiHandler() {
            @Override
            public String getPath() {
                return testPath;
            }

            @Override
            public String getAction() {
                return "testAction";
            }

            @Override
            public NativeApiResponse handleRequest(NativeApiRequest request) {
                return NativeApiResponse.success(request.getRequestId());
            }
        };

        assertFalse(router.canHandle(testPath));
        router.registerRoute(testPath, handler);
        assertTrue(router.canHandle(testPath));

        router.unregisterRoute(testPath);
        assertFalse(router.canHandle(testPath));
    }

    @Test
    public void testEchoCommandDispatchSync() {
        Map<String, Object> params = new HashMap<>();
        params.put("msg", "hello_world");
        NativeApiRequest request = new NativeApiRequest("req_1", "/echo", "echo", params, System.currentTimeMillis());

        NativeApiResponse response = router.dispatchSync(request);
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("req_1", response.getRequestId());
        assertNotNull(response.getData());
        assertEquals("echo", response.getData().get("echoAction"));
    }

    @Test
    public void testSystemDiagnosticDispatchSync() {
        NativeApiRequest request = new NativeApiRequest("req_diag", "/system/diagnostic", "diagnostic", new HashMap<>(), System.currentTimeMillis());
        NativeApiResponse response = router.dispatchSync(request);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
        assertEquals("HEALTHY", response.getData().get("healthStatus"));
        assertTrue(response.getData().containsKey("availableProcessors"));
    }

    @Test
    public void testRouteNotFound() {
        NativeApiRequest request = new NativeApiRequest("req_404", "/non/existent/path", "action", new HashMap<>(), System.currentTimeMillis());
        NativeApiResponse response = router.dispatchSync(request);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals(404, response.getStatusCode());
        assertNotNull(response.getError());
    }

    @Test
    public void testFallbackHandler() {
        IApiHandler fallback = new IApiHandler() {
            @Override
            public String getPath() {
                return "*";
            }

            @Override
            public String getAction() {
                return "fallback";
            }

            @Override
            public NativeApiResponse handleRequest(NativeApiRequest request) {
                Map<String, Object> data = new HashMap<>();
                data.put("handledBy", "fallback");
                return NativeApiResponse.success(request.getRequestId(), data);
            }
        };

        router.setFallbackHandler(fallback);
        NativeApiRequest request = new NativeApiRequest("req_fb", "/unmapped/route", "any", new HashMap<>(), System.currentTimeMillis());
        NativeApiResponse response = router.dispatchSync(request);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("fallback", response.getData().get("handledBy"));
    }

    @Test
    public void testAsyncDispatch() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean success = new AtomicBoolean(false);

        NativeApiRequest request = new NativeApiRequest("async_1", "/echo", "echo", new HashMap<>(), System.currentTimeMillis());
        router.dispatch(request, new IApiCallback() {
            @Override
            public void onResponse(NativeApiResponse response) {
                success.set(response.isSuccess());
                latch.countDown();
            }

            @Override
            public void onError(NativeApiError error) {
                latch.countDown();
            }
        });

        boolean completed = latch.await(5, TimeUnit.SECONDS);
        assertTrue(completed);
        assertTrue(success.get());
    }

    @Test
    public void testInterceptorPipelineExecutionOrder() {
        List<String> eventLog = new ArrayList<>();

        IRouteInterceptor interceptor1 = new IRouteInterceptor() {
            @Override
            public boolean preHandle(NativeApiRequest request, RouteMatchResult matchResult, IApiCallback callback) {
                eventLog.add("pre_1");
                return true;
            }

            @Override
            public void postHandle(NativeApiRequest request, NativeApiResponse response) {
                eventLog.add("post_1");
            }

            @Override
            public void onError(NativeApiRequest request, NativeApiError error) {
                eventLog.add("error_1");
            }
        };

        IRouteInterceptor interceptor2 = new IRouteInterceptor() {
            @Override
            public boolean preHandle(NativeApiRequest request, RouteMatchResult matchResult, IApiCallback callback) {
                eventLog.add("pre_2");
                return true;
            }

            @Override
            public void postHandle(NativeApiRequest request, NativeApiResponse response) {
                eventLog.add("post_2");
            }

            @Override
            public void onError(NativeApiRequest request, NativeApiError error) {
                eventLog.add("error_2");
            }
        };

        router.addInterceptor(interceptor1);
        router.addInterceptor(interceptor2);

        NativeApiRequest request = new NativeApiRequest("req_order", "/echo", "echo", new HashMap<>(), System.currentTimeMillis());
        NativeApiResponse response = router.dispatchSync(request);

        assertTrue(response.isSuccess());
        // preHandle executes in forward order (1, 2), postHandle executes in reverse order (2, 1)
        assertEquals(4, eventLog.size());
        assertEquals("pre_1", eventLog.get(0));
        assertEquals("pre_2", eventLog.get(1));
        assertEquals("post_2", eventLog.get(2));
        assertEquals("post_1", eventLog.get(3));
    }
}
