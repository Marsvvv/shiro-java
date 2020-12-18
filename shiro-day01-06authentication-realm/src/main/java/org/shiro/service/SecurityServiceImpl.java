package org.shiro.service;

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
        list.add("admin");
        list.add("dev");
        list.add("custom");
        return list;
    }

    @Override
    public List<String> findPermissionByLoginName(String loginName) {
        ArrayList<String> list = new ArrayList<>();
        list.add("order:add");
        list.add("order:list");
        list.add("order:del");
        return list;
    }
}
