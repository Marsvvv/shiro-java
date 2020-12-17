package org.shiro;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;

import java.util.HashMap;
import java.util.Map;

/**
 * Shiro 散列算法（不可解密的算法）
 * SHA、 MD5
 */
public class DigestUtil {

    private static final String algorithmName = "SHA-1";

    private static final Integer hashIterations = 512;

    /**
     * sha1加密
     *
     * @param input 明文密码
     * @param salt  盐
     * @return 加密密码
     */
    public static String sha1(String input, String salt) {
        //  algorithmName为加密方式
        //  hashInterations为加密次数
        return new SimpleHash(algorithmName, input, salt, hashIterations).toString();
    }

    /**
     * 返回基于 HEX加密 的随机盐值
     *
     * @return 基于 HEX加密 的随机盐值
     */
    public static String generateSalt() {
        SecureRandomNumberGenerator generator = new SecureRandomNumberGenerator();
        return generator.nextBytes().toHex();
    }

    /**
     * 生成 sha1 密码
     *
     * @param password 明文密码
     * @return sha1 密码
     */
    public static Map<String, String> entrptPassword(String password) {
        Map<String, String> map = new HashMap<>();

        String salt = generateSalt();
        String sha1 = sha1(password, salt);

        map.put("salt", salt);
        map.put("sha1", sha1);

        return map;
    }
}
