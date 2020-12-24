package org.shiro.demo.config;

import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.shiro.demo.core.ShiroDbRealm;
import org.shiro.demo.core.impl.ShiroDbRealmImpl;
import org.shiro.demo.properties.PropertiesUtils;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.Map;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = "org.shiro.demo.core")
public class ShiroConfig {

    /**
     * 创建cookie对象
     *
     * @return SimpleCookie
     */
    @Bean(name = "simpleCookie")
    public SimpleCookie simpleCookie() {
        return new SimpleCookie("ShiroSession");
    }

    /**
     * 创建权限管理器
     *
     * @return DefaultWebSecurityManager
     */
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        //  管理realm
        defaultWebSecurityManager.setRealm(shiroDbRealm());
        //  管理会话
        defaultWebSecurityManager.setSessionManager(sessionManager());

        return defaultWebSecurityManager;
    }

    /**
     * 自定义realm
     *
     * @return ShiroDbRealm
     */
    @Bean("shiroDbRealm")
    public ShiroDbRealm shiroDbRealm() {
        return new ShiroDbRealmImpl();
    }

    /**
     * 会话管理器
     *
     * @return DefaultWebSessionManager
     */
    @Bean("sessionManager")
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();

        //  指定cookie为开启状态
        defaultWebSessionManager.setSessionIdCookieEnabled(true);
        //  指定cookie的创建方式
        defaultWebSessionManager.setSessionIdCookie(simpleCookie());

        //  关闭会话更新
        defaultWebSessionManager.setSessionValidationSchedulerEnabled(false);

        //  设置会话全局超时时间
        defaultWebSessionManager.setGlobalSessionTimeout(60 * 60 * 1000);

        return defaultWebSessionManager;
    }

    //  以下配置为固定配置


    /**
     * 创建生命周期管理器
     *
     * @return LifecycleBeanPostProcessor
     */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    //  AOP增强

    /**
     * AOP式方法级权限检查
     *
     * @return DefaultAdvisorAutoProxyCreator
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    /**
     * 配合 DefaultAdvisorAutoProxyCreator 注解权限校验
     *
     * @return AuthorizationAttributeSourceAdvisor
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * Shiro过滤器管理
     *
     * @return AuthorizationAttributeSourceAdvisor
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager());
        //  过滤器

        //  过滤器链
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap());
        //  设置登录地址
        shiroFilterFactoryBean.setLoginUrl("/login");
        //  设置没有权限跳转的地址
        shiroFilterFactoryBean.setUnauthorizedUrl("/login");
        return shiroFilterFactoryBean;
    }

    private Map<String, String> filterChainDefinitionMap() {
        Properties properties = PropertiesUtils.readProperties("/authentication.properties");
        return PropertiesUtils.properties2Map(properties);
    }
}
