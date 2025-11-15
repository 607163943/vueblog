package com.project.blog.common.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.project.blog.common.constant.UserExceptionMessage;
import com.project.blog.common.exception.UserException;
import com.project.blog.common.properties.AccessTokenProperties;
import com.project.blog.common.properties.RefreshTokenProperties;
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

    // 刷新令牌属性
    @Resource
    private RefreshTokenProperties refreshTokenProperties;

    /**
     * 生成访问token
     */
    public String generateAccessToken(long userId) {
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
     * 生成令牌刷新用token
     */
    public String generateRefreshToken(long userId) {
        Date nowDate = new Date();
        //过期时间
        Date expireDate = new Date(nowDate.getTime() + refreshTokenProperties.getExpire() * 1000);
        Map<String, Object> map = new HashMap<>();
        map.put(TOKEN_HEADER_KEY, AccessTokenProperties.TYPE);

        return JWT.create().withHeader(map)
                // 这里转字符串
                .withSubject(userId + "")
                .withIssuedAt(nowDate)
                .withExpiresAt(expireDate)
                .sign(Algorithm.HMAC256(refreshTokenProperties.getSecret()));
    }

    /**
     * 获取token中的用户id
     *
     * @param token JWT令牌
     * @return 用户id
     */
    public String getUserId(String token) {
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(accessTokenProperties.getSecret())).build();
            return jwtVerifier.verify(token).getSubject();
        } catch (Exception e) {
            log.debug("validate is token error ", e);
            return null;
        }
    }

    /**
     * 验证token
     *
     * @param token JWT令牌
     */
    public void check(String token) {
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(accessTokenProperties.getSecret())).build();
            jwtVerifier.verify(token);
        } catch (Exception e) {
            log.info("JWT令牌过期", e);
            throw new UserException(UserExceptionMessage.LOGIN_TIMEOUT);
        }
    }
}
