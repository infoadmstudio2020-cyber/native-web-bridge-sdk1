package com.roni.library.security.permission;

/**
 * Representation of runtime permission status.
 *
 * @author infoadmstudio2020-cyber
 */
public enum PermissionStatus {
    GRANTED("granted"),
    DENIED("denied"),
    PERMANENTLY_DENIED("permanently_denied"),
    NOT_REQUESTED("not_requested");

    private final String value;

    PermissionStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public boolean isGranted() {
        return this == GRANTED;
    }

    public static PermissionStatus fromValue(String val) {
        if (val == null) return NOT_REQUESTED;
        for (PermissionStatus status : values()) {
            if (status.value.equalsIgnoreCase(val) || status.name().equalsIgnoreCase(val)) {
                return status;
            }
        }
        return NOT_REQUESTED;
    }
}
