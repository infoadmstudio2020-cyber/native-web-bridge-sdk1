package com.roni.library.security.crypto;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Cryptographic utility providing cryptographic hashing (SHA-256, SHA-512),
 * HMAC message signing, random salts, and timing-safe equality checks.
 *
 * @author infoadmstudio2020-cyber
 */
public final class CryptoHash {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private CryptoHash() {}

    public static byte[] generateRandomBytes(int length) {
        byte[] bytes = new byte[length];
        SECURE_RANDOM.nextBytes(bytes);
        return bytes;
    }

    public static String generateRandomSaltBase64(int length) {
        return Base64.getEncoder().encodeToString(generateRandomBytes(length));
    }

    public static byte[] sha256(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(data);
        } catch (Exception e) {
            throw new RuntimeException("SHA-256 algorithm unavailable", e);
        }
    }

    public static String sha256Hex(String input) {
        if (input == null) return "";
        byte[] hash = sha256(input.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hash);
    }

    public static byte[] sha512(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            return digest.digest(data);
        } catch (Exception e) {
            throw new RuntimeException("SHA-512 algorithm unavailable", e);
        }
    }

    public static String sha512Hex(String input) {
        if (input == null) return "";
        byte[] hash = sha512(input.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hash);
    }

    public static byte[] hmacSha256(byte[] data, byte[] key) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(key, "HmacSHA256");
            mac.init(secretKey);
            return mac.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException("HMAC-SHA256 operation failed", e);
        }
    }

    public static String hmacSha256Base64(String input, String secretKey) {
        byte[] result = hmacSha256(input.getBytes(StandardCharsets.UTF_8), secretKey.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(result);
    }

    public static boolean constantTimeEquals(byte[] a, byte[] b) {
        if (a == null || b == null) {
            return false;
        }
        if (a.length != b.length) {
            return false;
        }
        int result = 0;
        for (int i = 0; i < a.length; i++) {
            result |= a[i] ^ b[i];
        }
        return result == 0;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }
}
