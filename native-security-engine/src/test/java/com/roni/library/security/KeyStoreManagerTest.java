package com.roni.library.security;

import com.roni.library.security.keystore.KeyAlgorithm;
import com.roni.library.security.keystore.KeyStoreManager;

import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link KeyStoreManager} and cryptographic operations.
 *
 * @author infoadmstudio2020-cyber
 */
public class KeyStoreManagerTest {

    private KeyStoreManager manager;

    @Before
    public void setUp() {
        manager = new KeyStoreManager();
        manager.clear();
    }

    @Test
    public void testKeyGenerationAndList() {
        assertFalse(manager.hasKey("key_a"));
        assertTrue(manager.generateKey("key_a", KeyAlgorithm.AES_GCM_256, false));
        assertTrue(manager.hasKey("key_a"));
        assertEquals(KeyAlgorithm.AES_GCM_256, manager.getKeyAlgorithm("key_a"));

        assertTrue(manager.generateKey("key_rsa", KeyAlgorithm.RSA_2048, false));
        assertTrue(manager.hasKey("key_rsa"));

        List<String> aliases = manager.listAliases();
        assertEquals(2, aliases.size());
        assertTrue(aliases.contains("key_a"));
        assertTrue(aliases.contains("key_rsa"));

        assertTrue(manager.deleteKey("key_a"));
        assertFalse(manager.hasKey("key_a"));
        assertEquals(1, manager.listAliases().size());
    }

    @Test
    public void testAesGcmEncryptionDecryption() {
        String alias = "aes_gcm_test";
        manager.generateKey(alias, KeyAlgorithm.AES_GCM_256, false);

        String samplePlaintext = "TopSecretCredentials123!@#";
        Map<String, Object> enc = manager.encrypt(alias, samplePlaintext.getBytes(StandardCharsets.UTF_8));

        assertTrue(Boolean.TRUE.equals(enc.get("success")));
        assertNotNull(enc.get("ciphertext"));
        assertNotNull(enc.get("iv"));

        String stringPayload = manager.encryptString(alias, samplePlaintext);
        assertNotNull(stringPayload);
        assertTrue(stringPayload.contains(":"));

        String decrypted = manager.decryptString(alias, stringPayload);
        assertEquals(samplePlaintext, decrypted);
    }

    @Test
    public void testAlgorithmParsing() {
        assertEquals(KeyAlgorithm.AES_GCM_256, KeyAlgorithm.fromString("AES_GCM_256"));
        assertEquals(KeyAlgorithm.RSA_2048, KeyAlgorithm.fromString("RSA_2048"));
        assertEquals(KeyAlgorithm.AES_GCM_256, KeyAlgorithm.fromString("non_existent"));
    }
}
