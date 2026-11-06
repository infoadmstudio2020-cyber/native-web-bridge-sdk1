package com.roni.library.security.biometric;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.crypto.Cipher;

/**
 * Subsystem service handling biometric authentication capabilities,
 * prompt delegation, and observer callbacks.
 *
 * @author infoadmstudio2020-cyber
 */
public class BiometricAuthService {

    private boolean hardwareAvailable = true;
    private boolean biometricEnrolled = true;
    private BiometricPromptDelegate promptDelegate;
    private final List<BiometricAuthCallback> globalListeners = new CopyOnWriteArrayList<>();

    public void setHardwareAvailable(boolean available) {
        this.hardwareAvailable = available;
    }

    public void setBiometricEnrolled(boolean enrolled) {
        this.biometricEnrolled = enrolled;
    }

    public void setPromptDelegate(BiometricPromptDelegate delegate) {
        this.promptDelegate = delegate;
    }

    public BiometricCapability canAuthenticate(int authenticators) {
        if (!hardwareAvailable) {
            return BiometricCapability.ERROR_NO_HARDWARE;
        }
        if (!biometricEnrolled) {
            return BiometricCapability.ERROR_NONE_ENROLLED;
        }
        return BiometricCapability.SUCCESS;
    }

    public void authenticate(BiometricPromptConfig config, Cipher cipher, BiometricAuthCallback callback) {
        BiometricCapability capability = canAuthenticate(config != null ? config.getAllowedAuthenticators() : AuthenticatorType.BIOMETRIC_STRONG);
        if (!capability.isAvailable()) {
            if (callback != null) {
                callback.onAuthenticationError(capability.getCode(), capability.getDescription());
            }
            notifyGlobalError(capability.getCode(), capability.getDescription());
            return;
        }

        if (promptDelegate != null) {
            promptDelegate.showPrompt(config, cipher, callback);
        } else {
            // Simulated / headless execution for JVM testing & headless daemon environments
            onAuthenticationSucceeded("BIOMETRIC_STRONG", cipher);
            if (callback != null) {
                callback.onAuthenticationSucceeded("BIOMETRIC_STRONG", cipher);
            }
        }
    }

    public void authenticate(BiometricPromptConfig config, BiometricAuthCallback callback) {
        authenticate(config, null, callback);
    }

    public void onAuthenticationSucceeded(String authMethod, Cipher c) {
        for (BiometricAuthCallback listener : globalListeners) {
            try {
                listener.onAuthenticationSucceeded(authMethod, c);
            } catch (Exception ignored) {}
        }
    }

    public void onAuthenticationFailed(String reason) {
        for (BiometricAuthCallback listener : globalListeners) {
            try {
                listener.onAuthenticationFailed(reason);
            } catch (Exception ignored) {}
        }
    }

    public void onAuthenticationError(int errorCode, String errString) {
        notifyGlobalError(errorCode, errString);
    }

    private void notifyGlobalError(int errorCode, String errString) {
        for (BiometricAuthCallback listener : globalListeners) {
            try {
                listener.onAuthenticationError(errorCode, errString);
            } catch (Exception ignored) {}
        }
    }

    public void addGlobalListener(BiometricAuthCallback listener) {
        if (listener != null && !globalListeners.contains(listener)) {
            globalListeners.add(listener);
        }
    }

    public void removeGlobalListener(BiometricAuthCallback listener) {
        if (listener != null) {
            globalListeners.remove(listener);
        }
    }

    public void clear() {
        globalListeners.clear();
        promptDelegate = null;
    }
}
