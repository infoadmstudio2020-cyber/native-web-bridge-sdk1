package com.roni.library.security.biometric;

import javax.crypto.Cipher;

/**
 * Callback for receiving biometric authentication lifecycle results.
 *
 * @author infoadmstudio2020-cyber
 */
public interface BiometricAuthCallback {

    void onAuthenticationSucceeded(String authMethod, Cipher cipher);

    void onAuthenticationFailed(String reason);

    void onAuthenticationError(int errorCode, String errString);
}
