package org.shiro.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.shiro.service.SecurityService;
import org.shiro.service.SecurityServiceImpl;

/**
 * 自定义
 */
public class DefinitionRealm extends AuthorizingRealm {

    SecurityService securityService = new SecurityServiceImpl();

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
        String password = securityService.findPasswordByLoginName(loginName);
        //  判断密码是否正确
        if ("".equals(password)) {
            throw new UnknownAccountException("账户不存在");
        }
        //  返回
        return new SimpleAuthenticationInfo(loginName, password, getName());
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
