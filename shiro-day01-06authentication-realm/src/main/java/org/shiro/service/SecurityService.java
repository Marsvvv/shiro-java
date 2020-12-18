package org.shiro.service;

import java.util.List;
import java.util.Map;

/**
 * 模拟数据库操作
 */
public interface SecurityService {

    /**
     * 根据用户名查询密码
     *
     * @param loginName 用户名
     * @return 密码
     */
    Map<String, String> findPasswordByLoginName(String loginName);

    /**
     * 根据用户名查询角色
     *
     * @param loginName 用户名
     * @return 角色集合
     */
    List<String> findRoleByLoginName(String loginName);

    /**
     * 根据用户名查询权限（资源）
     *
     * @param loginName 用户名
     * @return 资源集合
     */
    List<String> findPermissionByLoginName(String loginName);
}
