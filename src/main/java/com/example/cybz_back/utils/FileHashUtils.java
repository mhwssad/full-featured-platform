package com.example.cybz_back.utils;

import com.example.cybz_back.enumerate.HashAlgorithm;
import org.apache.commons.codec.binary.Hex;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public final class FileHashUtils {
    private static final int BUFFER_SIZE = 8192;

    private FileHashUtils() {
        throw new AssertionError("工具类禁止实例化");
    }

    /**
     * 计算文件哈希（使用预定义算法）
     *
     * @param file      文件路径
     * @param algorithm 哈希算法
     * @return 哈希值字符串
     * @throws IOException              如果读取文件出错
     * @throws IllegalArgumentException 如果算法不支持
     */
    public static String calculateHash(Path file, HashAlgorithm algorithm) throws IOException {
        Objects.requireNonNull(file, "文件路径不能为null");
        Objects.requireNonNull(algorithm, "哈希算法不能为null");
        return calculateHash(file, algorithm.getAlgorithmName());
    }

    /**
     * 计算文件哈希（自定义算法名称）
     *
     * @param file      文件路径
     * @param algorithm 算法名称
     * @return 哈希值字符串
     * @throws IOException              如果读取文件出错
     * @throws IllegalArgumentException 如果算法不支持
     */
    public static String calculateHash(Path file, String algorithm) throws IOException {
        Objects.requireNonNull(file, "文件路径不能为null");
        Objects.requireNonNull(algorithm, "算法名称不能为null");

        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            return processStream(Files.newInputStream(file), digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("不支持的哈希算法: " + algorithm, e);
        }
    }

    /**
     * 计算字符串内容的哈希值
     *
     * @param content   字符串内容
     * @param algorithm 哈希算法
     * @return 哈希值字符串
     * @throws IOException 如果处理流出错
     */
    public static String hash(String content, HashAlgorithm algorithm) throws IOException {
        Objects.requireNonNull(content, "内容不能为null");
        return hash(content, algorithm.getAlgorithmName());
    }

    /**
     * 计算文件的哈希值
     *
     * @param file      文件对象
     * @param algorithm 哈希算法
     * @return 哈希值字符串
     * @throws IOException 如果读取文件出错
     */
    public static String hash(File file, HashAlgorithm algorithm) throws IOException {
        Objects.requireNonNull(file, "文件不能为null");
        return hash(file.toPath(), algorithm.getAlgorithmName());
    }

    /**
     * 计算输入流的哈希值
     *
     * @param inputStream 输入流
     * @param algorithm   哈希算法
     * @return 哈希值字符串
     * @throws IOException 如果读取流出错
     */
    public static String hash(InputStream inputStream, HashAlgorithm algorithm) throws IOException {
        Objects.requireNonNull(inputStream, "输入流不能为null");
        return hash(inputStream, algorithm.getAlgorithmName());
    }

    // ----------------------------- 内部方法 -----------------------------

    private static String hash(Object input, String algorithm) throws IOException {
        try (InputStream is = getInputStream(input)) {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            return processStream(is, digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("不支持的哈希算法: " + algorithm, e);
        }
    }

    private static InputStream getInputStream(Object input) throws IOException {
        if (input instanceof String str) {
            return new ByteArrayInputStream(str.getBytes());
        } else if (input instanceof Path path) {
            return Files.newInputStream(path);
        } else if (input instanceof File file) {
            return Files.newInputStream(file.toPath());
        } else if (input instanceof InputStream is) {
            return is;
        } else {
            throw new IllegalArgumentException("不支持的输入类型: " + input.getClass().getName());
        }
    }

    private static String processStream(InputStream is, MessageDigest digest) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = is.read(buffer)) != -1) {
            digest.update(buffer, 0, bytesRead);
        }
        return Hex.encodeHexString(digest.digest());
    }

    // ----------------------------- 快捷方法 -----------------------------

    public static String md5(Path file) throws IOException {
        return calculateHash(file, HashAlgorithm.MD5);
    }

    public static String sha256(Path file) throws IOException {
        return calculateHash(file, HashAlgorithm.SHA_256);
    }

    public static String sha512(Path file) throws IOException {
        return calculateHash(file, HashAlgorithm.SHA_512);
    }

    // 字符串快捷方式
    public static String md5(String content) throws IOException {
        return hash(content, HashAlgorithm.MD5);
    }

    public static String sha256(String content) throws IOException {
        return hash(content, HashAlgorithm.SHA_256);
    }

    public static String sha512(String content) throws IOException {
        return hash(content, HashAlgorithm.SHA_512);
    }

    // 文件对象快捷方式
    public static String md5(File file) throws IOException {
        return hash(file, HashAlgorithm.MD5);
    }

    public static String sha256(File file) throws IOException {
        return hash(file, HashAlgorithm.SHA_256);
    }

    public static String sha512(File file) throws IOException {
        return hash(file, HashAlgorithm.SHA_512);
    }

    // 输入流快捷方式
    public static String md5(InputStream is) throws IOException {
        return hash(is, HashAlgorithm.MD5);
    }

    public static String sha256(InputStream is) throws IOException {
        return hash(is, HashAlgorithm.SHA_256);
    }

    public static String sha512(InputStream is) throws IOException {
        return hash(is, HashAlgorithm.SHA_512);
    }
}