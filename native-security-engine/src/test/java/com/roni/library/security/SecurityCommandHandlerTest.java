package com.roni.library.security;

import com.roni.library.contracts.api.IApiCallback;
import com.roni.library.contracts.api.NativeApiError;
import com.roni.library.contracts.api.NativeApiRequest;
import com.roni.library.contracts.api.NativeApiResponse;
import com.roni.library.security.handler.SecurityCommandHandler;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link SecurityCommandHandler} Layer 0 RPC contract dispatching.
 *
 * @author infoadmstudio2020-cyber
 */
public class SecurityCommandHandlerTest {

    private SecurityCommandHandler handler;

    @Before
    public void setUp() {
        SecurityEngine.getInstance().initialize();
        handler = new SecurityCommandHandler();
    }

    @Test
    public void testRoutePrefix() {
        assertEquals("/security", handler.getRoutePrefix());
        assertEquals("/security", handler.getPath());
        assertEquals("*", handler.getAction());
    }

    @Test
    public void testKeyStoreGenerateAndEncrypt() {
        Map<String, Object> genParams = new HashMap<>();
        genParams.put("alias", "rpc_key");
        genParams.put("algorithm", "AES_GCM_256");

        NativeApiRequest genReq = new NativeApiRequest("req_1", "/security", "keystore.generate", genParams, System.currentTimeMillis());
        NativeApiResponse genRes = handler.handleRequest(genReq);
        assertTrue(genRes.isSuccess());

        Map<String, Object> encParams = new HashMap<>();
        encParams.put("alias", "rpc_key");
        encParams.put("plaintext", "SecureRpcSecret");

        NativeApiRequest encReq = new NativeApiRequest("req_2", "/security", "keystore.encrypt", encParams, System.currentTimeMillis());
        NativeApiResponse encRes = handler.handleRequest(encReq);
        assertTrue(encRes.isSuccess());
        assertNotNull(encRes.getData().get("ciphertext"));
        assertNotNull(encRes.getData().get("iv"));
    }

    @Test
    public void testBiometricCapabilityCommand() {
        NativeApiRequest req = new NativeApiRequest("req_bio", "/security", "biometric.capability", new HashMap<>(), System.currentTimeMillis());
        NativeApiResponse res = handler.handleRequest(req);
        assertTrue(res.isSuccess());
        assertTrue(res.getData().containsKey("available"));
    }

    @Test
    public void testDiagnosticCommand() {
        NativeApiRequest req = new NativeApiRequest("req_diag", "/security", "diagnostic", new HashMap<>(), System.currentTimeMillis());
        NativeApiResponse res = handler.handleRequest(req);
        assertTrue(res.isSuccess());
        assertTrue(res.getData().containsKey("state"));
        assertTrue(res.getData().containsKey("registeredKeysCount"));
    }

    @Test
    public void testAsyncHandle() {
        AtomicBoolean called = new AtomicBoolean(false);
        NativeApiRequest req = new NativeApiRequest("req_async", "/security", "diagnostic", new HashMap<>(), System.currentTimeMillis());

        handler.handle(req, new IApiCallback() {
            @Override
            public void onResponse(NativeApiResponse response) {
                called.set(true);
                assertTrue(response.isSuccess());
            }

            @Override
            public void onError(NativeApiError error) {}
        });

        assertTrue(called.get());
    }
}
