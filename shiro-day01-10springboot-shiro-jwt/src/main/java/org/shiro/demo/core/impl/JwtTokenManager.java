package org.shiro.demo.core.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.shiro.demo.properties.JwtProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 自定义JwtToken管理器
 *
 * @author Tobu
 */
@Component
@EnableConfigurationProperties(JwtProperties.class)
public class JwtTokenManager {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * JWT三大组成部分
     * header: 1.Type 2.加密算法名
     * payload: 1.过期时间 2.签发人 3.Subject 4.audience
     * sign: 签名
     *
     * @return sign
     */
    public String sign(String sessionId, String issuer, Long outOfMilli) {
        String sign = JWT.create()
                .withIssuer(issuer)
                .withJWTId(sessionId)
                .withExpiresAt(new Date(System.currentTimeMillis() + outOfMilli))
                .sign(Algorithm.HMAC256(jwtProperties.getBase64EncodedSecretKey()));
        return sign;
    }

    public DecodedJWT decode(String token) {
        return JWT.decode(token);
    }

    public Boolean verify(String token) {
        try {
            //  验证失败会抛出异常
            JWT.require(Algorithm.HMAC256(jwtProperties.getBase64EncodedSecretKey())).build().verify(token);
        } catch (JWTVerificationException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return true;
    }
}
