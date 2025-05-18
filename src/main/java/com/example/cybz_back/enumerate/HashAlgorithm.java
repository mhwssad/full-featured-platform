package com.example.cybz_back.enumerate;

/**
 * 预定义标准哈希算法枚举（可按需扩展）
 */
public enum HashAlgorithm {
    MD5("MD5"),
    SHA_1("SHA-1"),
    SHA_256("SHA-256"),
    SHA_384("SHA-384"),
    SHA_512("SHA-512"),
    SHA3_256("SHA3-256");

    private final String algorithmName;

    HashAlgorithm(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }
}
