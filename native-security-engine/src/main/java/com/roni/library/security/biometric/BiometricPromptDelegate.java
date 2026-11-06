package com.roni.library.security.biometric;

import javax.crypto.Cipher;

/**
 * Delegate interface for presenting biometric prompt UI from Activities.
 *
 * @author infoadmstudio2020-cyber
 */
public interface BiometricPromptDelegate {

    void showPrompt(BiometricPromptConfig config, Cipher cipher, BiometricAuthCallback callback);
}
