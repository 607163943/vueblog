package com.project.blog.common.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "security.jwt.access-token")
public class AccessTokenProperties {
    public static final String TYPE="access";
    // 密钥
    private String secret;

    // 有效时间(s)
    private Long expire;

    // 长期有效时间(s)
    private Long longExpire;
}
