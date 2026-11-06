
package com.roni.library.security;

import com.roni.library.contracts.common.IDisposable;
import com.roni.library.contracts.common.RuntimeState;

import com.roni.library.security.biometric.BiometricAuthService;
import com.roni.library.security.handler.SecurityCommandHandler;
import com.roni.library.security.keystore.KeyStoreManager;
import com.roni.library.security.permission.PermissionService;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Main coordinator and thread-safe singleton state machine
 * for NativeSecurityEngine.
 *
 * Implements IDisposable lifecycle contract.
 *
 * @author infoadmstudio2020-cyber
 */
public class SecurityEngine implements IDisposable {

    private static volatile SecurityEngine sInstance;

    private volatile RuntimeState state = RuntimeState.UNINITIALIZED;

    private final AtomicBoolean disposed = new AtomicBoolean(false);

    private volatile SecurityEngineConfig config;

    private final KeyStoreManager keyStoreManager;
    private final BiometricAuthService biometricService;
    private final PermissionService permissionService;
    private final SecurityCommandHandler commandHandler;

    /**
     * Private constructor for singleton.
     */
    private SecurityEngine() {

        this.keyStoreManager = new KeyStoreManager();
        this.biometricService = new BiometricAuthService();
        this.permissionService = new PermissionService();
        this.commandHandler = new SecurityCommandHandler();
    }

    /**
     * Returns the singleton instance.
     */
    public static SecurityEngine getInstance() {

        if (sInstance == null) {

            synchronized (SecurityEngine.class) {

                if (sInstance == null) {
                    sInstance = new SecurityEngine();
                }
            }
        }

        return sInstance;
    }

    /**
     * Initializes the security engine.
     */
    public synchronized void init(
            Object context,
            SecurityEngineConfig config) {

        checkNotDisposed();

        if (state == RuntimeState.READY) {
            return;
        }

        state = RuntimeState.INITIALIZING;

        try {

            this.config = config != null
                    ? config
                    : new SecurityEngineConfig.Builder().build();

            state = RuntimeState.READY;

        } catch (RuntimeException e) {

            state = RuntimeState.UNINITIALIZED;

            throw e;
        }
    }

    /**
     * Initializes the engine using default configuration.
     */
    public synchronized void initialize() {

        init(
                null,
                new SecurityEngineConfig.Builder().build()
        );
    }

    /**
     * Checks whether the engine is initialized.
     */
    public boolean isInitialized() {

        return !disposed.get()
                && state == RuntimeState.READY;
    }

    /**
     * Returns current runtime state.
     */
    public RuntimeState getState() {
        return state;
    }

    /**
     * Returns current configuration.
     */
    public SecurityEngineConfig getConfig() {
        return config;
    }

    /**
     * Updates engine configuration.
     */
    public synchronized void setConfig(SecurityEngineConfig config) {

        checkNotDisposed();

        if (config == null) {
            throw new IllegalArgumentException(
                    "SecurityEngineConfig cannot be null"
            );
        }

        this.config = config;
    }

    /**
     * Returns KeyStoreManager.
     */
    public KeyStoreManager getKeyStoreManager() {

        checkNotDisposed();

        return keyStoreManager;
    }

    /**
     * Returns BiometricAuthService.
     */
    public BiometricAuthService getBiometricService() {

        checkNotDisposed();

        return biometricService;
    }

    /**
     * Returns PermissionService.
     */
    public PermissionService getPermissionService() {

        checkNotDisposed();

        return permissionService;
    }

    /**
     * Returns SecurityCommandHandler.
     */
    public SecurityCommandHandler getCommandHandler() {

        checkNotDisposed();

        return commandHandler;
    }

    /**
     * Checks whether this engine has been disposed.
     */
    @Override
    public boolean isDisposed() {
        return disposed.get();
    }

    /**
     * Releases all resources and terminates the engine.
     *
     * This operation is idempotent.
     */
    @Override
    public synchronized void dispose() {

        if (!disposed.compareAndSet(false, true)) {
            return;
        }

        state = RuntimeState.TERMINATED;

        try {

            if (biometricService != null) {
                biometricService.clear();
            }

        } catch (RuntimeException ignored) {
            // Continue releasing remaining resources.
        }

        try {

            if (permissionService != null) {
                permissionService.clear();
            }

        } catch (RuntimeException ignored) {
            // Continue disposal.
        }
    }

    /**
     * Prevents operations after disposal.
     */
    private void checkNotDisposed() {

        if (disposed.get()) {

            throw new IllegalStateException(
                    "SecurityEngine has already been disposed"
            );
        }
    }
}
