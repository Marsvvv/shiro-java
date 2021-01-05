package org.shiro.demo.core.impl;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.redisson.api.RedissonClient;
import org.shiro.demo.constant.CacheConstant;
import org.shiro.demo.constant.CredentialConstant;
import org.shiro.demo.core.ShiroDbRealm;
import org.shiro.demo.core.SimpleCacheService;
import org.shiro.demo.core.base.ShiroUser;
import org.shiro.demo.core.base.SimpleToken;
import org.shiro.demo.core.bridge.UserBridgeService;
import org.shiro.demo.entity.User;
import org.shiro.demo.util.ShiroUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ShiroDbRealmImpl extends ShiroDbRealm {

    @Autowired
    UserBridgeService userBridgeService;

    @Autowired
    SimpleCacheService simpleCacheService;

    @Autowired
    RedissonClient redissonClient;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        ShiroUser shiroUser = (ShiroUser) principalCollection.getPrimaryPrincipal();
        return userBridgeService.getAuthorizationInfo(shiroUser);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //  获取令牌信息
        SimpleToken simpleToken = (SimpleToken) authenticationToken;
        //  查询User对象
        User user = userBridgeService.findUserByLoginName(simpleToken.getUsername());
        //  构建认证对象
        ShiroUser shiroUser = new ShiroUser();
        BeanUtils.copyProperties(user, shiroUser);
        List<String> resourceIds = userBridgeService.findResourceIdsByUserId(shiroUser.getId());
        shiroUser.setResourceIds(resourceIds);
        //  构建认证信息
        return new SimpleAuthenticationInfo(shiroUser, shiroUser.getPassWord(), ByteSource.Util.bytes(shiroUser.getSalt()), getName());
    }

    @Override
    public void initCredentialsMatcher() {
        /*HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashIterations(CredentialConstant.HASH_ITERATIONS);
        hashedCredentialsMatcher.setHashAlgorithmName(CredentialConstant.HASH_ALGORITHM);
        setCredentialsMatcher(hashedCredentialsMatcher);*/

        //  自定义密码比较器
        RetryLimitCredentialsMatcher retryLimitCredentialsMatcher = new RetryLimitCredentialsMatcher(redissonClient);
        retryLimitCredentialsMatcher.setHashIterations(CredentialConstant.HASH_ITERATIONS);
        retryLimitCredentialsMatcher.setHashAlgorithmName(CredentialConstant.HASH_ALGORITHM);
        setCredentialsMatcher(retryLimitCredentialsMatcher);
    }

    /**
     * 退出登录后清除缓存
     *
     * @param principals principals
     */
    @Override
    protected void doClearCache(PrincipalCollection principals) {
        ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
        String sessionId = ShiroUtil.getShiroSessionId();
        String resourceIdsKey = CacheConstant.RESOURCES_KEY_IDS + sessionId;
        String roleKey = CacheConstant.ROLE_KEY + sessionId;
        String key = CacheConstant.FIND_USER_BY_LOGINNAME + shiroUser.getLoginName();
        simpleCacheService.removeCache(roleKey);
        simpleCacheService.removeCache(resourceIdsKey);
        simpleCacheService.removeCache(key);
        super.doClearCache(principals);
    }
}
