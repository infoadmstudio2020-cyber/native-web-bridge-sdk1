package com.roni.library.security;

import com.roni.library.security.crypto.CryptoHash;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link CryptoHash}.
 *
 * @author infoadmstudio2020-cyber
 */
public class CryptoHashTest {

    @Test
    public void testSha256Determinism() {
        String input = "HelloWorld";
        String hash1 = CryptoHash.sha256Hex(input);
        String hash2 = CryptoHash.sha256Hex(input);
        assertNotNull(hash1);
        assertEquals(64, hash1.length());
        assertEquals(hash1, hash2);

        String different = CryptoHash.sha256Hex("DifferentWorld");
        assertNotEquals(hash1, different);
    }

    @Test
    public void testHmacSha256() {
        String key = "super_secret_key";
        String payload = "data=123";
        String sig1 = CryptoHash.hmacSha256Base64(payload, key);
        String sig2 = CryptoHash.hmacSha256Base64(payload, key);
        assertNotNull(sig1);
        assertEquals(sig1, sig2);
    }

    @Test
    public void testConstantTimeEquals() {
        byte[] a = "secret".getBytes();
        byte[] b = "secret".getBytes();
        byte[] c = "public".getBytes();

        assertTrue(CryptoHash.constantTimeEquals(a, b));
        assertFalse(CryptoHash.constantTimeEquals(a, c));
        assertFalse(CryptoHash.constantTimeEquals(a, null));
    }
}
