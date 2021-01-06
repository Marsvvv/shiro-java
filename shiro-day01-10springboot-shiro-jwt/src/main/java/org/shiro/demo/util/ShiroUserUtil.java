
package org.shiro.demo.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.shiro.demo.constant.SuperConstant;
import org.shiro.demo.core.base.ShiroUser;


/**
 * shiroUser工具类
 */
public class ShiroUserUtil extends ShiroUtil {

    /**
     * 返回当前登录用户封装对象
     *
     * @return ShiroUser
     */
    public static ShiroUser getShiroUser() {
        //System.out.println(SecurityUtils.getSubject());
        if (!BeanUtil.isEmpty(ThreadContext.getSubject()) && !BeanUtil.isEmpty(SecurityUtils.getSubject().getPrincipal())) {
            return (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        } else {
            return new ShiroUser(SuperConstant.ANON_ID, SuperConstant.ANON_LOGIN_NAME);
        }
    }

    /**
     * 获得shiroUserId
     *
     * @return String
     */
    public static String getShiroUserId() {
        ShiroUser shiroUser = ShiroUserUtil.getShiroUser();
        if (BeanUtil.isEmpty(shiroUser)) {
            return null;
        } else {
            return shiroUser.getId();
        }
    }


    /**
     * 更新登录用户信息  用户id 和用户登录名不更新
     *
     * @param shiroUser 需要修改对象
     */
    public static void updateShiroUser(ShiroUser shiroUser) {
        String subjectKey = "org.apache.shiro.subject.support.DefaultSubjectContext_PRINCIPALS_SESSION_KEY";
        Subject subject = SecurityUtils.getSubject();
        Object ooo = subject.getSession().getAttribute(subjectKey);
        SimplePrincipalCollection collection = (SimplePrincipalCollection) ooo;
        ShiroUser user = (ShiroUser) collection.getPrimaryPrincipal();
        subject.getSession().setAttribute(subjectKey, collection);
    }

}
