package com.project.blog.shiro;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authc.AuthenticationToken;

@Builder
@Data
@RequiredArgsConstructor
public class JWTToken implements AuthenticationToken {
    // JWT令牌
    private final String token;

    @Override
    public Object getPrincipal() {
        return token;
    }
    @Override
    public Object getCredentials() {
        return token;
    }
}