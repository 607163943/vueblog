package com.project.blog.common.properties;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@EqualsAndHashCode(callSuper = true)
@Data
@Component
@ConfigurationProperties(prefix = "security.jwt.access-token")
public class AccessTokenProperties extends BaseTokenProperties {
    public static final String TYPE="access";
    // 密钥
    private String secret;

    // 过期时间(s)
    private Long expire;
}
