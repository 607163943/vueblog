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
     * @return 16进制的随机盐
     */
    public static String generateSalt() {
        return new SecureRandomNumberGenerator().nextBytes().toHex();
    }

    /**
     * 密码加密
     * @param frontendHashedPassword 前端传递的密码
     * @param salt 盐值
     * @return 加密后的密码
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
     * @param frontendHashedPassword 前端传递的密码
     * @param salt 盐值
     * @param storedPassword 数据库存储密码
     * @return 匹配结果
     */
    public static boolean matches(String frontendHashedPassword, String salt, String storedPassword) {
        String newPassword = encryptPassword(frontendHashedPassword, salt);
        return storedPassword.equals(newPassword);
    }
}
