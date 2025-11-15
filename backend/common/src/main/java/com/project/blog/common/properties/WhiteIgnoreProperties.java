package com.project.blog.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "security.white-ignore")
public class WhiteIgnoreProperties {
    // 忽略的uri
    private List<Ignore> ignores;
}
