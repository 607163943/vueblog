package com.project.blog.common.utils;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class SecurityUtils {
    // 使用加密算法
    private static final String HASH_ALGORITHM_NAME = "SHA-256";

    // 迭代次数
    private static final int HASH_ITERATIONS = 1024;


    /**
     * 生成随机盐
     *
     * @return
     */
    public static String generateSalt() {
        return new SecureRandomNumberGenerator().nextBytes().toHex();
    }

    /**
     * 密码加密
     * @param frontendHashedPassword
     * @param salt
     * @return
     */
    public static String encryptPassword(String frontendHashedPassword, String salt) {
        return new SimpleHash(
                HASH_ALGORITHM_NAME,
                frontendHashedPassword,
                ByteSource.Util.bytes(salt),
                HASH_ITERATIONS)
                .toHex();
    }

    /**
     * 密码匹配
     * @param frontendHashedPassword
     * @param salt
     * @param storedPassword
     * @return
     */
    public static boolean matches(String frontendHashedPassword, String salt, String storedPassword) {
        String newPassword = encryptPassword(frontendHashedPassword, salt);
        return storedPassword.equals(newPassword);
    }
}
