package com.project.blog.common.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JWTUtils {

    private String secret;
    private long expire;
    private String header;

    /**
     * 生成jwt token
     */
    public String generateToken(long userId) {
        Date nowDate = new Date();
        //过期时间
        Date expireDate = new Date(nowDate.getTime() + expire * 1000);
        Map<String,Object> map=new HashMap<>();
        map.put("typ", "JWT");

        return JWT.create().withHeader(map)
                .withSubject(userId+"")
                .withIssuedAt(nowDate)
                .withExpiresAt(expireDate)
                .sign(Algorithm.HMAC256( secret));
    }

    /**
     * 获取token中的用户id
     * @param token
     * @return
     */
    public String getUserId(String token) {
        try{
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secret)).build();
            return jwtVerifier.verify(token).getSubject();
        }catch (Exception e){
            log.debug("validate is token error ", e);
            return null;
        }
    }

    /**
     * 验证token
     * @param token
     */
    public void check(String token) {
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secret)).build();
            jwtVerifier.verify(token);
        }catch (Exception e){
            log.debug("validate is token error ", e);
            throw new RuntimeException("token已过期");
        }
    }

    /**
     * token是否过期
     * @return  true：过期
     */
    public boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }
}
