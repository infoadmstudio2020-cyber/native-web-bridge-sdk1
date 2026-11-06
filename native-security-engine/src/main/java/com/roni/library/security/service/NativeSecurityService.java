
package com.roni.library.security.service;

import com.roni.library.security.SecurityEngine;
import com.roni.library.security.biometric.BiometricAuthService;
import com.roni.library.security.keystore.KeyStoreManager;
import com.roni.library.security.permission.PermissionService;

import com.roni.library.service.BaseService;

/**
 * NativeSecurityEngine integration service.
 *
 * Manages SecurityEngine lifecycle through BaseService.
 *
 * @author infoadmstudio2020-cyber
 */
public class NativeSecurityService extends BaseService {

    public static final String SERVICE_NAME =
            "NativeSecurityService";

    private final SecurityEngine engine;

    public NativeSecurityService() {
        this(SecurityEngine.getInstance());
    }

    public NativeSecurityService(SecurityEngine engine) {

        super(SERVICE_NAME);

        if (engine == null) {
            throw new IllegalArgumentException(
                    "SecurityEngine cannot be null"
            );
        }

        this.engine = engine;
    }

    /**
     * Initializes the security engine.
     */
    @Override
    protected void onInitialize() {

        if (engine.isDisposed()) {
            throw new IllegalStateException(
                    "SecurityEngine is already disposed"
            );
        }

        if (!engine.isInitialized()) {
            engine.initialize();
        }
    }

    /**
     * Releases the security engine.
     */
    @Override
    protected void onDispose() {

        if (!engine.isDisposed()) {
            engine.dispose();
        }
    }

    public SecurityEngine getSecurityEngine() {
        return engine;
    }

    public PermissionService getPermissionService() {
        return engine.getPermissionService();
    }

    public BiometricAuthService getBiometricService() {
        return engine.getBiometricService();
    }

    public KeyStoreManager getKeyStoreManager() {
        return engine.getKeyStoreManager();
    }
}
