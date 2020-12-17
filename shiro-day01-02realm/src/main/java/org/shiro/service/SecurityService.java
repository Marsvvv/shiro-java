package org.shiro.service;

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
    String findPasswordByLoginName(String loginName);
}
