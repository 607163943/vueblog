package com.project.blog.common.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseTokenProperties {
    // 密钥
    private String secret;

    // 过期时间(s)
    private Long expire;
}
