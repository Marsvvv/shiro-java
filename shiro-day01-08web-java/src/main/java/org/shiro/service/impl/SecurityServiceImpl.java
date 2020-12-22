package org.shiro.service.impl;

import org.shiro.service.SecurityService;
import org.shiro.util.DigestUtil;

import java.util.ArrayList;
import java.util.List;
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

    @Override
    public List<String> findRoleByLoginName(String loginName) {
        ArrayList<String> list = new ArrayList<>();
        if ("admin".equals(loginName)) {
            list.add("admin");
        }
        list.add("dev");
        return list;
    }

    @Override
    public List<String> findPermissionByLoginName(String loginName) {
        ArrayList<String> list = new ArrayList<>();
        if ("jay".equals(loginName)) {
            list.add("order:add");
            list.add("order:list");
            list.add("order:del");
        }
        return list;
    }
}
