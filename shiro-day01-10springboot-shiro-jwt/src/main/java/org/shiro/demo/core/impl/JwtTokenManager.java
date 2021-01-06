package org.shiro.demo.core.impl;

import cn.hutool.core.bean.BeanUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.shiro.demo.properties.JwtProperties;
import org.shiro.demo.util.EncodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 自定义JWT管理器
 *
 * @author asus
 */
@Service
@EnableConfigurationProperties(JwtProperties.class)
public class JwtTokenManager {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 签发令牌
     * 1.header信息： 1.密码 2.加密算法
     * 2.payload： 1.签发时间 2.唯一标识 3.签发人 4.过期时间
     *
     * @param issuer     签发者
     * @param ttlMillis  签发时间
     * @param sessionId  sessionId
     * @param claims     jwt一些非隐私信息
     * @param privateKey 私钥
     * @return String
     */
    public String issuedToken(String issuer, Long ttlMillis, String sessionId, Map<String, Object> claims, String privateKey) {
        //  判断 claims 是否为空， 为空则初始化
        if (BeanUtil.isEmpty(claims)) {
            claims = new HashMap<>(0);
        }
        //  获取当前时间戳
        long nowMillis = System.currentTimeMillis();
        //  获得用 HEX 加密的 签名
        String base64EncodeSecretKey = EncodeUtil.encodeHex(jwtProperties.getBase64EncodedSecretKey().getBytes(StandardCharsets.UTF_8));
        //  构建令牌
        JWTCreator.Builder builder = JWT.create();
        Iterator<Map.Entry<String, Object>> iterator = claims.entrySet().iterator();
        while (iterator.hasNext()) {
            builder.withClaim(iterator.next().getKey(), (String) iterator.next().getValue());
        }
        return builder
                //  构建唯一标识，此时生成shiro唯一id
                .withJWTId(sessionId)
                //  生成签发时间
                .withIssuedAt(new Date(nowMillis))
                //  签发者
                .withSubject(issuer)
                //  指定过期时间
                .withExpiresAt(new Date(nowMillis + ttlMillis))
                //  指定算法和密钥
                .sign(Algorithm.HMAC256(base64EncodeSecretKey));
    }

    /**
     * 解析令牌
     *
     * @param jwtToken jwtToken
     * @return DecodedJWT
     */
    public DecodedJWT decodeToken(String jwtToken) {
        //  获取加密签名
        String base64EncodeSecretKey = EncodeUtil.encodeHex(jwtProperties.getBase64EncodedSecretKey().getBytes(StandardCharsets.UTF_8));
        //  带着密码去解析字符串
        return JWT.require(Algorithm.HMAC256(jwtToken)).build().verify(jwtToken);
    }

    /**
     * 校验令牌
     *
     * @param decodedJWT decodedJWT
     * @return boolean
     */
    public boolean verify(DecodedJWT decodedJWT) {
        //TODO
        return false;
    }

}
