package org.shiro.demo.core;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.PostConstruct;

public abstract class ShiroDbRealm extends AuthorizingRealm {

    /**
     * 授权方法
     *
     * @param principalCollection 令牌对象
     * @return 授权信息
     */
    @Override
    protected abstract AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection);

    /**
     * 认证方法
     *
     * @param authenticationToken token对象
     * @return 认证信息
     * @throws AuthenticationException AuthenticationException
     */
    @Override
    protected abstract AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException;

    /**
     * 自定义密码比较器
     *
     * @PostConstruct 初始化时自动初始化
     */
    @PostConstruct
    public abstract void initCredentialsMatcher();
}
