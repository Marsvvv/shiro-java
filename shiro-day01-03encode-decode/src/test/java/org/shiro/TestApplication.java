package org.shiro;

import org.junit.Test;

public class TestApplication {

    @Test
    public void hexTest() {
        String pwd = "123";

        String encodeHex = EncodeUtil.encodeHex(pwd.getBytes());

        byte[] bytes = EncodeUtil.decodeHex(encodeHex);

        System.out.println("解密密码为：" + new String(bytes));
    }

    @Test
    public void base64Test() {
        String pwd = "123";

        String encodeHex = EncodeUtil.encodeBase64(pwd.getBytes());

        byte[] bytes = EncodeUtil.decodeBase64(encodeHex);

        System.out.println("解密密码为：" + new String(bytes));
    }
}
