package org.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Test;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.shiro.util.DigestUtil;

import java.util.Map;

public class TestApplication {


    public Subject login() {

        IniSecurityManagerFactory factory = new IniSecurityManagerFactory("classpath:shiro.ini");

        SecurityManager securityManager = factory.getInstance();

        SecurityUtils.setSecurityManager(securityManager);

        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("jay", "123");

        subject.login(token);

        System.out.println("登录：" + subject.isAuthenticated());

        return subject;
    }

    @Test
    public void TestPermission() {
        //  用户是否登录成功
        Subject subject = login();
        //  校验当前用户是否拥有角色
        System.out.println("是否有管理员角色：" + subject.hasRole("admin"));
        //  校验当前用户有没有的角色
        try {
            subject.checkRole("coder");
            System.out.println("当前用户有coder角色");
        } catch (AuthorizationException e) {
            System.out.println("当前用户没有coder角色");
        }
        //  校验当前用户是否拥有权限
        System.out.println("是否有权限：" + subject.isPermitted("order:list"));
        //  校验当前用户的权限
        try {
            subject.checkPermission("order:edit");
            System.out.println("当前用户有edit权限");
        } catch (AuthorizationException e) {
            System.out.println("当前用户没有edit权限");
        }
    }
}
