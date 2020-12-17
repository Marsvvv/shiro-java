package org.shiro.util;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.Hex;

/**
 * 对 Shiro 编码和解码的封装工具类
 * Base64 和 Hex
 */
public class EncodeUtil {

    /**
     * hex 编码
     *
     * @param input 明文密码
     * @return 加密密码
     */
    public static String encodeHex(byte[] input) {
        return Hex.encodeToString(input);
    }

    /**
     * hex 解码
     *
     * @param input 加密密码
     * @return 明文密码
     */
    public static byte[] decodeHex(String input) {
        return Hex.decode(input);
    }

    /**
     * Base64 编码
     *
     * @param input 明文密码
     * @return 加密密码
     */
    public static String encodeBase64(byte[] input) {
        return Base64.encodeToString(input);
    }

    /**
     * Base64 解码
     *
     * @param input 加密密码
     * @return 明文密码
     */
    public static byte[] decodeBase64(String input) {
        return Base64.decode(input);
    }
}
