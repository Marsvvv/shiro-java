package org.shiro.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.shiro.service.SecurityService;
import org.shiro.service.SecurityServiceImpl;
import org.shiro.util.DigestUtil;

import java.util.Map;

/**
 * 自定义
 */
public class DefinitionRealm extends AuthorizingRealm {

    SecurityService securityService = new SecurityServiceImpl();

    public DefinitionRealm() {

        //  指定密码匹配方式 （这次用的是SHA1）
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher(DigestUtil.algorithmName);

        //  指定密码加密次数
        hashedCredentialsMatcher.setHashIterations(DigestUtil.hashIterations);

        //  使用父类方法使得匹配方式生效
        setCredentialsMatcher(hashedCredentialsMatcher);
    }

    /**
     * 自定义认证方法
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //  获取提交的账户用户名
        String loginName = (String) authenticationToken.getPrincipal();
        //  查询密码
        Map<String, String> map = securityService.findPasswordByLoginName(loginName);
        //  判断密码是否正确
        if (null == map) {
            throw new UnknownAccountException("账户不存在");
        }
        String salt = map.get("salt");
        String password = map.get("password");
        //  返回
        return new SimpleAuthenticationInfo(loginName, password, ByteSource.Util.bytes(salt), getName());
    }

    /**
     * 自定义鉴权方法
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }
}
