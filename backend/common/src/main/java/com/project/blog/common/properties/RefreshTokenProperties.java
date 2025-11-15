package com.project.blog.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "security.jwt.refresh-token")
public class RefreshTokenProperties {
    public static final String TYPE = "refresh";

    // 密钥
    private String secret;

    // 过期时间(s)
    private Long expire;
}
