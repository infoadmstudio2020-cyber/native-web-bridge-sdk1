package com.roni.library.security.keystore;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * Manages cryptographic keys, hardware security module interaction,
 * AES-GCM/CBC encryption, and decryption operations.
 *
 * @author infoadmstudio2020-cyber
 */
public class KeyStoreManager {

    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 128;

    private final Map<String, Key> fallbackKeyStore = new ConcurrentHashMap<>();
    private final Map<String, KeyAlgorithm> algorithmRegistry = new ConcurrentHashMap<>();
    private final SecureRandom secureRandom = new SecureRandom();
    private boolean hardwareSecurityModuleAvailable = true;

    public KeyStoreManager() {
        initInternal();
    }

    private void initInternal() {
        try {
            KeyStore ks = KeyStore.getInstance(ANDROID_KEY_STORE);
            ks.load(null);
        } catch (Exception e) {
            // In headless/JVM test environments, AndroidKeyStore is substituted by fallbackKeyStore
            hardwareSecurityModuleAvailable = false;
        }
    }

    public boolean isHardwareSecurityModuleAvailable() {
        return hardwareSecurityModuleAvailable;
    }

    public void setHardwareSecurityModuleAvailable(boolean available) {
        this.hardwareSecurityModuleAvailable = available;
    }

    public synchronized boolean generateKey(String alias, KeyAlgorithm algorithm, boolean requiresUserAuth) {
        if (alias == null || alias.trim().isEmpty() || algorithm == null) {
            return false;
        }

        try {
            if (algorithm == KeyAlgorithm.RSA_2048 || algorithm == KeyAlgorithm.RSA_4096) {
                KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
                kpg.initialize(algorithm.getKeySize(), secureRandom);
                KeyPair kp = kpg.generateKeyPair();
                fallbackKeyStore.put(alias, kp.getPrivate());
            } else {
                KeyGenerator kg = KeyGenerator.getInstance(algorithm.getStandardName());
                kg.init(algorithm.getKeySize(), secureRandom);
                SecretKey secretKey = kg.generateKey();
                fallbackKeyStore.put(alias, secretKey);
            }
            algorithmRegistry.put(alias, algorithm);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasKey(String alias) {
        if (alias == null) return false;
        return fallbackKeyStore.containsKey(alias);
    }

    public boolean deleteKey(String alias) {
        if (alias == null) return false;
        algorithmRegistry.remove(alias);
        return fallbackKeyStore.remove(alias) != null;
    }

    public List<String> listAliases() {
        return Collections.unmodifiableList(new ArrayList<>(fallbackKeyStore.keySet()));
    }

    public KeyAlgorithm getKeyAlgorithm(String alias) {
        return algorithmRegistry.getOrDefault(alias, KeyAlgorithm.AES_GCM_256);
    }

    public Cipher getInitializedCipher(String alias, int mode, byte[] iv) {
        Key key = fallbackKeyStore.get(alias);
        if (key == null) {
            throw new IllegalStateException("Key not found for alias: " + alias);
        }

        KeyAlgorithm algo = getKeyAlgorithm(alias);
        try {
            Cipher cipher = Cipher.getInstance(algo.getTransformation());
            if (algo == KeyAlgorithm.AES_GCM_256) {
                if (iv == null) {
                    iv = new byte[GCM_IV_LENGTH];
                    secureRandom.nextBytes(iv);
                }
                GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
                cipher.init(mode, key, spec);
            } else if (algo == KeyAlgorithm.AES_CBC_256) {
                if (iv == null) {
                    iv = new byte[16];
                    secureRandom.nextBytes(iv);
                }
                IvParameterSpec spec = new IvParameterSpec(iv);
                cipher.init(mode, key, spec);
            } else {
                cipher.init(mode, key);
            }
            return cipher;
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize cipher for alias: " + alias, e);
        }
    }

    public Map<String, Object> encrypt(String alias, byte[] plaintext) {
        if (!hasKey(alias)) {
            generateKey(alias, KeyAlgorithm.AES_GCM_256, false);
        }

        KeyAlgorithm algo = getKeyAlgorithm(alias);
        Map<String, Object> result = new HashMap<>();
        try {
            byte[] iv = new byte[algo == KeyAlgorithm.AES_GCM_256 ? GCM_IV_LENGTH : 16];
            secureRandom.nextBytes(iv);

            Cipher cipher = getInitializedCipher(alias, Cipher.ENCRYPT_MODE, iv);
            byte[] ciphertext = cipher.doFinal(plaintext);

            result.put("success", true);
            result.put("ciphertext", Base64.getEncoder().encodeToString(ciphertext));
            result.put("iv", Base64.getEncoder().encodeToString(iv));
            result.put("algorithm", algo.name());
            result.put("alias", alias);
            return result;
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
            return result;
        }
    }

    public byte[] decrypt(String alias, byte[] ciphertext, byte[] iv) {
        if (!hasKey(alias)) {
            throw new IllegalArgumentException("No key registered for alias: " + alias);
        }

        try {
            Cipher cipher = getInitializedCipher(alias, Cipher.DECRYPT_MODE, iv);
            return cipher.doFinal(ciphertext);
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed: " + e.getMessage(), e);
        }
    }

    public String encryptString(String alias, String plaintext) {
        if (plaintext == null) return null;
        Map<String, Object> enc = encrypt(alias, plaintext.getBytes(StandardCharsets.UTF_8));
        if (Boolean.TRUE.equals(enc.get("success"))) {
            return enc.get("ciphertext") + ":" + enc.get("iv");
        }
        return null;
    }

    public String decryptString(String alias, String combinedPayload) {
        if (combinedPayload == null || !combinedPayload.contains(":")) return null;
        String[] parts = combinedPayload.split(":", 2);
        byte[] ciphertext = Base64.getDecoder().decode(parts[0]);
        byte[] iv = Base64.getDecoder().decode(parts[1]);
        byte[] decrypted = decrypt(alias, ciphertext, iv);
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    public void clear() {
        fallbackKeyStore.clear();
        algorithmRegistry.clear();
    }
}
