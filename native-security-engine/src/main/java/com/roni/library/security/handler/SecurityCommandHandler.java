package com.roni.library.security.handler;

import androidx.annotation.NonNull;

import com.roni.library.contracts.api.IApiHandler;
import com.roni.library.contracts.api.NativeApiError;
import com.roni.library.contracts.api.NativeApiRequest;
import com.roni.library.contracts.api.NativeApiResponse;
import com.roni.library.security.SecurityEngine;
import com.roni.library.security.biometric.AuthenticatorType;
import com.roni.library.security.biometric.BiometricCapability;
import com.roni.library.security.biometric.BiometricPromptConfig;
import com.roni.library.security.crypto.CryptoHash;
import com.roni.library.security.keystore.KeyAlgorithm;
import com.roni.library.security.keystore.KeyStoreManager;
import com.roni.library.security.permission.PermissionStatus;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;

/**
 * Universal RPC Command Handler implementing Layer 0 IApiHandler.
 * <p>
 * Dispatches commands under the "/security" route prefix.
 *
 * @author infoadmstudio2020-cyber
 */
public class SecurityCommandHandler implements IApiHandler {

    private static final String ROUTE_PREFIX = "/security";

    private static final String ERROR_INVALID_REQUEST = "INVALID_REQUEST";
    private static final String ERROR_INVALID_ACTION = "INVALID_ACTION";
    private static final String ERROR_COMMAND_FAILED = "SECURITY_COMMAND_ERROR";

    public SecurityCommandHandler() {
        // Default constructor.
    }

    @NonNull
    private static String getString(
            @NonNull Map<String, Object> params,
            @NonNull String key,
            @NonNull String defaultValue) {

        Object value = params.get(key);

        return value != null
                ? String.valueOf(value)
                : defaultValue;
    }

    @NonNull
    private static String requireString(
            @NonNull Map<String, Object> params,
            @NonNull String key) {

        Object value = params.get(key);

        if (!(value instanceof String)
                || ((String) value).trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Missing or invalid parameter: " + key
            );
        }

        return ((String) value).trim();
    }

    private static boolean getBoolean(
            @NonNull Map<String, Object> params,
            @NonNull String key,
            boolean defaultValue) {

        Object value = params.get(key);

        if (value instanceof Boolean) {
            return (Boolean) value;
        }

        if (value instanceof String) {
            return Boolean.parseBoolean((String) value);
        }

        return defaultValue;
    }

    private static int getInt(
            @NonNull Map<String, Object> params,
            @NonNull String key,
            int defaultValue) {

        Object value = params.get(key);

        if (value instanceof Number) {
            return ((Number) value).intValue();
        }

        if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(
                        "Invalid integer parameter: " + key
                );
            }
        }

        return defaultValue;
    }

    @NonNull
    public String getRoutePrefix() {
        return ROUTE_PREFIX;
    }

    // ---------------------------------------------------------
    // PARAMETER HELPERS
    // ---------------------------------------------------------

    @NonNull
    @Override
    public String getPath() {
        return ROUTE_PREFIX;
    }

    @NonNull
    @Override
    public String getAction() {
        return "*";
    }

    /**
     * Main API command entry point.
     */
    @NonNull
    @Override
    public NativeApiResponse handleRequest(
            @NonNull NativeApiRequest request) {

        if (request == null) {
            return NativeApiResponse.error(
                    "",
                    400,
                    new NativeApiError(
                            ERROR_INVALID_REQUEST,
                            "Request cannot be null"
                    )
            );
        }

        try {
            String action = request.getAction();

            if (action == null || action.trim().isEmpty()) {
                return NativeApiResponse.error(
                        request.getRequestId(),
                        400,
                        new NativeApiError(
                                ERROR_INVALID_ACTION,
                                "Security action cannot be empty"
                        )
                );
            }

            Map<String, Object> params = request.getParameters();

            if (params == null) {
                params = Collections.emptyMap();
            }

            Map<String, Object> data = dispatchCommand(
                    action,
                    params
            );

            return NativeApiResponse.success(
                    request.getRequestId(),
                    data
            );

        } catch (IllegalArgumentException e) {

            return NativeApiResponse.error(
                    request.getRequestId(),
                    400,
                    new NativeApiError(
                            ERROR_INVALID_REQUEST,
                            e.getMessage() != null
                                    ? e.getMessage()
                                    : "Invalid security command parameters"
                    )
            );

        } catch (Exception e) {

            return NativeApiResponse.error(
                    request.getRequestId(),
                    500,
                    new NativeApiError(
                            ERROR_COMMAND_FAILED,
                            "Security command dispatch error",
                            e.getMessage()
                    )
            );
        }
    }

    /**
     * Dispatches supported security commands.
     */
    @NonNull
    private Map<String, Object> dispatchCommand(
            @NonNull String action,
            @NonNull Map<String, Object> params) {

        Map<String, Object> data = new HashMap<>();

        SecurityEngine engine = SecurityEngine.getInstance();

        if (engine == null) {
            throw new IllegalStateException(
                    "SecurityEngine is not initialized"
            );
        }

        KeyStoreManager ksm = engine.getKeyStoreManager();

        if (ksm == null) {
            throw new IllegalStateException(
                    "KeyStoreManager is not available"
            );
        }

        switch (action) {

            // -------------------------------------------------
            // KEYSTORE GENERATE
            // -------------------------------------------------

            case "keystore.generate": {

                String alias = getString(
                        params,
                        "alias",
                        "default_key"
                );

                String algoStr = getString(
                        params,
                        "algorithm",
                        "AES_GCM_256"
                );

                boolean userAuth = getBoolean(
                        params,
                        "requiresUserAuth",
                        false
                );

                KeyAlgorithm algo = KeyAlgorithm.fromString(algoStr);

                if (algo == null) {
                    throw new IllegalArgumentException(
                            "Unsupported key algorithm: " + algoStr
                    );
                }

                boolean success = ksm.generateKey(
                        alias,
                        algo,
                        userAuth
                );

                data.put("success", success);
                data.put("alias", alias);
                data.put("algorithm", algo.name());

                break;
            }

            // -------------------------------------------------
            // KEYSTORE HAS KEY
            // -------------------------------------------------

            case "keystore.hasKey": {

                String alias = requireString(params, "alias");

                data.put("hasKey", ksm.hasKey(alias));
                data.put("alias", alias);

                break;
            }

            // -------------------------------------------------
            // KEYSTORE DELETE
            // -------------------------------------------------

            case "keystore.delete": {

                String alias = requireString(params, "alias");

                data.put("deleted", ksm.deleteKey(alias));
                data.put("alias", alias);

                break;
            }

            // -------------------------------------------------
            // KEYSTORE LIST
            // -------------------------------------------------

            case "keystore.list": {

                List<String> aliases = ksm.listAliases();

                data.put(
                        "aliases",
                        aliases != null ? aliases : Collections.emptyList()
                );

                break;
            }

            // -------------------------------------------------
            // KEYSTORE ENCRYPT
            // -------------------------------------------------

            case "keystore.encrypt": {

                String alias = getString(
                        params,
                        "alias",
                        "default_key"
                );

                Object rawText = params.get("plaintext");

                if (rawText == null) {
                    throw new IllegalArgumentException(
                            "Missing plaintext parameter"
                    );
                }

                byte[] plaintextBytes;

                if (rawText instanceof String) {

                    plaintextBytes = (
                            (String) rawText
                    ).getBytes(StandardCharsets.UTF_8);

                } else if (rawText instanceof byte[]) {

                    plaintextBytes = (byte[]) rawText;

                } else {

                    throw new IllegalArgumentException(
                            "Plaintext must be a String or byte[]"
                    );
                }

                Map<String, Object> encResult = ksm.encrypt(
                        alias,
                        plaintextBytes
                );

                if (encResult != null) {
                    data.putAll(encResult);
                }

                data.put("success", encResult != null);

                break;
            }

            // -------------------------------------------------
            // KEYSTORE DECRYPT
            // -------------------------------------------------


            case "keystore.decrypt": {

                String alias = requireString(params, "alias");
                String cipherB64 = requireString(params, "ciphertext");
                String ivB64 = requireString(params, "iv");

                try {

                    byte[] cipherBytes = android.util.Base64.decode(
                            cipherB64,
                            android.util.Base64.DEFAULT
                    );

                    byte[] ivBytes = android.util.Base64.decode(
                            ivB64,
                            android.util.Base64.DEFAULT
                    );

                    byte[] decrypted = ksm.decrypt(
                            alias,
                            cipherBytes,
                            ivBytes
                    );

                    if (decrypted == null) {
                        throw new IllegalStateException(
                                "Decryption returned null"
                        );
                    }

                    data.put(
                            "plaintext",
                            new String(
                                    decrypted,
                                    StandardCharsets.UTF_8
                            )
                    );

                    data.put("success", true);

                } catch (IllegalArgumentException e) {

                    throw new IllegalArgumentException(
                            "Invalid Base64 ciphertext or IV"
                    );
                }

                break;
            }


            // -------------------------------------------------
            // BIOMETRIC CAPABILITY
            // -------------------------------------------------

            case "biometric.capability": {

                int auth = getInt(
                        params,
                        "authenticators",
                        AuthenticatorType.BIOMETRIC_STRONG
                );

                BiometricCapability capability =
                        engine.getBiometricService()
                                .canAuthenticate(auth);

                if (capability == null) {
                    throw new IllegalStateException(
                            "Biometric capability result is unavailable"
                    );
                }

                data.put("code", capability.getCode());
                data.put("description", capability.getDescription());
                data.put("available", capability.isAvailable());

                break;
            }

            // -------------------------------------------------
            // BIOMETRIC AUTHENTICATION
            // -------------------------------------------------

            case "biometric.authenticate": {

                String title = getString(
                        params,
                        "title",
                        "Biometric Authentication"
                );

                BiometricPromptConfig config =
                        BiometricPromptConfig.builder()
                                .setTitle(title)
                                .build();

                /*
                 * Authentication is asynchronous.
                 * Do not treat an immediate empty result as success.
                 *
                 * The current IApiHandler contract returns a synchronous
                 * NativeApiResponse, so this command reports that the
                 * authentication request was initiated.
                 */

                engine.getBiometricService().authenticate(
                        config,
                        new com.roni.library.security.biometric.BiometricAuthCallback() {

                            @Override
                            public void onAuthenticationSucceeded(
                                    String authMethod,
                                    Cipher cipher) {
                                // Authentication callback.
                            }

                            @Override
                            public void onAuthenticationFailed(
                                    String reason) {
                                // Authentication failure callback.
                            }

                            @Override
                            public void onAuthenticationError(
                                    int errorCode,
                                    String errString) {
                                // Authentication error callback.
                            }
                        }
                );

                data.put("success", true);
                data.put("initiated", true);
                data.put("message", "Biometric authentication initiated");

                break;
            }

            // -------------------------------------------------
            // PERMISSION CHECK
            // -------------------------------------------------

            case "permission.check": {

                String permission = requireString(
                        params,
                        "permission"
                );

                PermissionStatus status =
                        engine.getPermissionService()
                                .checkPermission(permission);

                if (status == null) {
                    throw new IllegalStateException(
                            "Permission status is unavailable"
                    );
                }

                data.put("permission", permission);
                data.put("status", status.getValue());
                data.put("granted", status.isGranted());

                break;
            }

            // -------------------------------------------------
            // SHA-256 HASH
            // -------------------------------------------------

            case "hash.sha256": {

                String input = getString(
                        params,
                        "input",
                        ""
                );

                data.put(
                        "sha256",
                        CryptoHash.sha256Hex(input)
                );

                break;
            }

            // -------------------------------------------------
            // DIAGNOSTIC
            // -------------------------------------------------

            case "diagnostic": {

                data.put("state", engine.getState().name());

                data.put(
                        "hsmAvailable",
                        ksm.isHardwareSecurityModuleAvailable()
                );

                data.put(
                        "biometricAvailable",
                        engine.getBiometricService()
                                .canAuthenticate(
                                        AuthenticatorType.BIOMETRIC_STRONG
                                )
                                .isAvailable()
                );

                List<String> aliases = ksm.listAliases();

                data.put(
                        "registeredKeysCount",
                        aliases != null ? aliases.size() : 0
                );

                data.put("routePrefix", ROUTE_PREFIX);

                data.put("success", true);

                break;
            }

            // -------------------------------------------------
            // UNKNOWN COMMAND
            // -------------------------------------------------

            default: {

                throw new IllegalArgumentException(
                        "Unsupported security action: " + action
                );
            }
        }

        return data;
    }
}
