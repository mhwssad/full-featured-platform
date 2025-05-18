package com.example.cybz_back.utils;

import com.example.cybz_back.enumerate.HashAlgorithm;
import org.apache.commons.codec.binary.Hex;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class FileHashUtils {

    private static final int BUFFER_SIZE = 8192;

    // 禁止实例化
    private FileHashUtils() {
        throw new AssertionError("工具类禁止实例化");
    }


    /**
     * 计算文件哈希（使用预定义算法）
     */
    public static String calculateHash(Path file, HashAlgorithm algorithm) throws IOException {
        return calculateHash(file, algorithm.getAlgorithmName());
    }

    /**
     * 计算文件哈希（自定义算法名称）
     */
    public static String calculateHash(Path file, String algorithm) throws IOException {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            return processStream(file, digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("不支持的哈希算法: " + algorithm, e);
        }
    }

    //----------------------------- 核心处理逻辑 -----------------------------//
    private static String processStream(Path file, MessageDigest digest) throws IOException {
        try (InputStream is = Files.newInputStream(file)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
            return Hex.encodeHexString(digest.digest());
        }
    }

    /**
     * 快速访问方法 - 常用算法快捷方式
     */
    public static String md5(Path file) throws IOException {
        return calculateHash(file, HashAlgorithm.MD5);
    }

    public static String sha256(Path file) throws IOException {
        return calculateHash(file, HashAlgorithm.SHA_256);
    }

    public static String sha512(Path file) throws IOException {
        return calculateHash(file, HashAlgorithm.SHA_512);
    }
}