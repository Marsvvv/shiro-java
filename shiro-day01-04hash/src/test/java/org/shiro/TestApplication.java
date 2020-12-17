package org.shiro;

import org.junit.Test;

import java.util.Map;

public class TestApplication {

    @Test
    public void hexTest() {
        String pwd = "123";

        Map<String, String> map = DigestUtil.entrptPassword(pwd);

        System.out.println(map.toString());
    }
}
