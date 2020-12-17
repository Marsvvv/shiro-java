package org.shiro.service;

import org.shiro.util.DigestUtil;

import java.util.Map;

/**
 * 实现类
 *
 * @author Tobu
 */
public class SecurityServiceImpl implements SecurityService {

    @Override
    public Map<String, String> findPasswordByLoginName(String loginName) {
        return DigestUtil.entrptPassword("123");
    }
}
