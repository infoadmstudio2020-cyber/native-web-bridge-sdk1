package com.roni.library.security;

import com.roni.library.security.biometric.AuthenticatorType;
import com.roni.library.security.biometric.BiometricAuthCallback;
import com.roni.library.security.biometric.BiometricAuthService;
import com.roni.library.security.biometric.BiometricCapability;
import com.roni.library.security.biometric.BiometricPromptConfig;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import javax.crypto.Cipher;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link BiometricAuthService}.
 *
 * @author infoadmstudio2020-cyber
 */
public class BiometricAuthServiceTest {

    private BiometricAuthService service;

    @Before
    public void setUp() {
        service = new BiometricAuthService();
    }

    @Test
    public void testCapabilityChecks() {
        service.setHardwareAvailable(true);
        service.setBiometricEnrolled(true);
        assertEquals(BiometricCapability.SUCCESS, service.canAuthenticate(AuthenticatorType.BIOMETRIC_STRONG));

        service.setHardwareAvailable(false);
        assertEquals(BiometricCapability.ERROR_NO_HARDWARE, service.canAuthenticate(AuthenticatorType.BIOMETRIC_STRONG));

        service.setHardwareAvailable(true);
        service.setBiometricEnrolled(false);
        assertEquals(BiometricCapability.ERROR_NONE_ENROLLED, service.canAuthenticate(AuthenticatorType.BIOMETRIC_STRONG));
    }

    @Test
    public void testAuthenticationFlow() {
        service.setHardwareAvailable(true);
        service.setBiometricEnrolled(true);

        AtomicBoolean successCalled = new AtomicBoolean(false);
        BiometricPromptConfig config = BiometricPromptConfig.builder()
                .setTitle("Test Auth")
                .build();

        service.authenticate(config, new BiometricAuthCallback() {
            @Override
            public void onAuthenticationSucceeded(String authMethod, Cipher cipher) {
                successCalled.set(true);
                assertEquals("BIOMETRIC_STRONG", authMethod);
            }

            @Override
            public void onAuthenticationFailed(String reason) {}

            @Override
            public void onAuthenticationError(int errorCode, String errString) {}
        });

        assertTrue(successCalled.get());
    }

    @Test
    public void testGlobalListeners() {
        AtomicBoolean globalCalled = new AtomicBoolean(false);
        BiometricAuthCallback listener = new BiometricAuthCallback() {
            @Override
            public void onAuthenticationSucceeded(String authMethod, Cipher cipher) {
                globalCalled.set(true);
            }

            @Override
            public void onAuthenticationFailed(String reason) {}

            @Override
            public void onAuthenticationError(int errorCode, String errString) {}
        };

        service.addGlobalListener(listener);
        service.onAuthenticationSucceeded("GLOBAL_TEST", null);
        assertTrue(globalCalled.get());

        service.removeGlobalListener(listener);
    }
}
