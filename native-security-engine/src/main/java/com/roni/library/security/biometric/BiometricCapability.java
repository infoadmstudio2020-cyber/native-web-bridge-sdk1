package com.roni.library.security.biometric;

/**
 * Biometric authentication capability and hardware status.
 *
 * @author infoadmstudio2020-cyber
 */
public enum BiometricCapability {
    SUCCESS(0, "Biometric authentication is available"),
    ERROR_HW_UNAVAILABLE(1, "Biometric hardware is currently unavailable"),
    ERROR_NONE_ENROLLED(11, "No biometric features enrolled on device"),
    ERROR_NO_HARDWARE(12, "Device does not possess biometric hardware"),
    ERROR_SECURITY_UPDATE_REQUIRED(15, "Security update required for biometric hardware");

    private final int code;
    private final String description;

    BiometricCapability(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public boolean isAvailable() {
        return this == SUCCESS;
    }
}
