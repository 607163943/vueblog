package com.project.blog.common.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.project.blog.common.constant.UserExceptionMessage;
import com.project.blog.common.exception.UserLoginException;
import com.project.blog.common.properties.AccessTokenProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Data
@Component
public class JWTUtils {

    private static final String TOKEN_HEADER_KEY = "type";

    // 访问令牌属性
    @Resource
    private AccessTokenProperties accessTokenProperties;


    /**
     * 生成访问token
     */
    public String generateToken(long userId) {
        Date nowDate = new Date();
        //过期时间
        Date expireDate = new Date(nowDate.getTime() + accessTokenProperties.getExpire() * 1000);
        Map<String, Object> map = new HashMap<>();
        map.put(TOKEN_HEADER_KEY, AccessTokenProperties.TYPE);

        return JWT.create().withHeader(map)
                .withSubject(userId + "")
                .withIssuedAt(nowDate)
                .withExpiresAt(expireDate)
                .sign(Algorithm.HMAC256(accessTokenProperties.getSecret()));
    }

    /**
     * 获取访问token中的用户id
     *
     * @param token JWT令牌
     * @return 用户id
     */
    public String getUserId(String token) {
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(accessTokenProperties.getSecret())).build();
            return jwtVerifier.verify(token).getSubject();
        } catch (Exception e) {
            log.info("JWT令牌过期", e);
            throw new UserLoginException(UserExceptionMessage.LOGIN_TIMEOUT);
        }
    }

    /**
     * 验证访问token
     *
     * @param token JWT令牌
     */
    public void check(String token) {
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(accessTokenProperties.getSecret())).build();
            jwtVerifier.verify(token);
        } catch (Exception e) {
            log.info("JWT令牌过期", e);
            throw new UserLoginException(UserExceptionMessage.LOGIN_TIMEOUT);
        }
    }
}
