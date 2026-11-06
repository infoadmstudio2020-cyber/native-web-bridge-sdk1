package com.roni.library.security.biometric;

/**
 * Constants and bitwise flags for allowed authenticators.
 *
 * @author infoadmstudio2020-cyber
 */
public final class AuthenticatorType {

    public static final int BIOMETRIC_STRONG = 0x000F;
    public static final int BIOMETRIC_WEAK = 0x00FF;
    public static final int DEVICE_CREDENTIAL = 0x8000;

    private AuthenticatorType() {}

    public static boolean allowsDeviceCredential(int authenticators) {
        return (authenticators & DEVICE_CREDENTIAL) != 0;
    }

    public static boolean allowsBiometricStrong(int authenticators) {
        return (authenticators & BIOMETRIC_STRONG) != 0;
    }
}
