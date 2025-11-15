package com.project.blog.common.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@EqualsAndHashCode(callSuper = true)
@Data
@Component
@ConfigurationProperties(prefix = "security.jwt.refresh-token")
public class RefreshTokenProperties extends BaseTokenProperties {
    public static final String TYPE = "refresh";
}
