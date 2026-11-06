package com.roni.library.security.keystore;

/**
 * Supported cryptographic algorithms for the Hardware KeyStore manager.
 *
 * @author infoadmstudio2020-cyber
 */
public enum KeyAlgorithm {
    AES_GCM_256("AES", 256, "AES/GCM/NoPadding"),
    AES_CBC_256("AES", 256, "AES/CBC/PKCS5Padding"),
    RSA_2048("RSA", 2048, "RSA/ECB/PKCS1Padding"),
    RSA_4096("RSA", 4096, "RSA/ECB/PKCS1Padding"),
    HMAC_SHA256("HmacSHA256", 256, "HmacSHA256");

    private final String standardName;
    private final int keySize;
    private final String transformation;

    KeyAlgorithm(String standardName, int keySize, String transformation) {
        this.standardName = standardName;
        this.keySize = keySize;
        this.transformation = transformation;
    }

    public String getStandardName() {
        return standardName;
    }

    public int getKeySize() {
        return keySize;
    }

    public String getTransformation() {
        return transformation;
    }

    public static KeyAlgorithm fromString(String name) {
        if (name == null || name.trim().isEmpty()) {
            return AES_GCM_256;
        }
        for (KeyAlgorithm algo : values()) {
            if (algo.name().equalsIgnoreCase(name) || algo.standardName.equalsIgnoreCase(name)) {
                return algo;
            }
        }
        return AES_GCM_256;
    }
}
