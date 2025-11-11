package com.project.blog;

import cn.hutool.crypto.digest.MD5;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestUtils {
    @Test
    void testMd5Utils() {
        // 测试hutool工具包MD5加密
        System.out.println(MD5.create().digestHex("a"));
    }
}
