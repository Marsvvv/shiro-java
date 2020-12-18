package org.shiro.service;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 登录服务
 */
public interface LoginService {

    /**
     * 登录
     *
     * @param usernamePasswordToken token
     * @return 登录是否成功
     */
    boolean login(UsernamePasswordToken usernamePasswordToken);

    /**
     * 登出
     */
    void logout();
}
