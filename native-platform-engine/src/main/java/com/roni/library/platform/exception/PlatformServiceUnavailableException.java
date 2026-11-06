package com.roni.library.platform.exception;

/**
 * Exception thrown when a requested platform subsystem or service is unavailable.
 *
 * @author infoadmstudio2020-cyber
 */
public class PlatformServiceUnavailableException extends PlatformEngineException {
    private final String serviceName;

    public PlatformServiceUnavailableException(String serviceName) {
        super("Platform service '" + serviceName + "' is unavailable or uninitialized.");
        this.serviceName = serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }
}
