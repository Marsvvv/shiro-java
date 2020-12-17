package org.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * Shiro Hello World
 */
public class HelloShiro {

    @Test
    public void shiroLogin() {

        //  导入 shiro.ini 配置创建工厂
        IniSecurityManagerFactory factory = new IniSecurityManagerFactory("classpath:shiro.ini");

        //  工厂构建 SecurityManager 安全管理器
        SecurityManager securityManager = factory.getInstance();

        //  调用工具类使 SecurityManager 安全管理器生效
        SecurityUtils.setSecurityManager(securityManager);

        //  使用工具获取 Subject 主体
        Subject subject = SecurityUtils.getSubject();

        //  构建账户和密码
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("jay", "123");

        //  使用 Subject 主体登录
        subject.login(usernamePasswordToken);

        //  打印登录信息
        System.out.println("登录结果：" + subject.isAuthenticated());
    }
}
