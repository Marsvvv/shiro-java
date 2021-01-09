package org.shiro.demo.core.bridge;


import org.apache.shiro.authz.AuthorizationInfo;
import org.shiro.demo.core.base.ShiroUser;
import org.shiro.demo.entity.User;

import java.util.List;

/**
 * 用户信息桥接(后期做缓存)
 */
public interface UserBridgeService {

    /**
     * 查找用户信息
     *
     * @param loginName 登录名
     * @return 用户信息
     */
    User findUserByLoginName(String loginName);

    /**
     * 根据用户id查询资源id列表
     *
     * @param userId 用户id
     * @return 资源id列表
     */
    List<String> findResourceIdsByUserId(String userId);

    /**
     * 鉴权方法
     *
     * @param shiroUser 令牌对象
     * @return 鉴权信息
     */
    AuthorizationInfo getAuthorizationInfo(ShiroUser shiroUser);
}
