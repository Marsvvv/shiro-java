package org.shiro.demo.config;

import lombok.extern.log4j.Log4j2;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.shiro.demo.core.ShiroDbRealm;
import org.shiro.demo.core.filter.CustomFilter;
import org.shiro.demo.core.impl.ShiroDbRealmImpl;
import org.shiro.demo.properties.LinkedProperties;
import org.shiro.demo.properties.PropertiesUtils;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author asus
 * @EnableConfigurationProperties 允许 ShiroReidsProperties 使用 @ConfigurationProperties 可以用来读取properties文件属性
 */
@Configuration
@ComponentScan(basePackages = "org.shiro.demo.core")
@EnableConfigurationProperties({ShiroRedisProperties.class})
public class ShiroConfig {

    @Autowired
    private ShiroRedisProperties shiroRedisProperties;

    /**
     * 为什么要使用缓存 Realm
     * 因为鉴权在我们的请求占比中非常高，每一次访问请求我们都需要鉴权，访问相应数据库
     * 对数据库产生非常大的压力，所以使用 Redis缓存 进行优化
     * 鉴权 Redis和 业务Redis也建议分开存放，降低系统压力
     *
     * @return RedissonClient
     */
    @Bean("redissonClientForShiro")
    public RedissonClient redissonClient(){
        //获取当前redis节点信息
        String[] nodes = shiroRedisProperties.getNodes().split(",");
        //创建配置信息：1、单机redis配置 2、集群redis配置
        Config config = new Config();
        if (nodes.length==1){
            //1、单机redis配置
            config.useSingleServer().setAddress(nodes[0])
                    .setConnectTimeout(shiroRedisProperties.getConnectTimeout())
                    .setConnectionMinimumIdleSize(shiroRedisProperties.getConnectionMinimumidleSize())
                    .setConnectionPoolSize(shiroRedisProperties.getConnectPoolSize())
                    .setTimeout(shiroRedisProperties.getTimeout());
        }else if(nodes.length>1) {
            //2、集群redis配置
            config.useClusterServers().addNodeAddress(nodes)
                    .setConnectTimeout(shiroRedisProperties.getConnectTimeout())
                    .setMasterConnectionMinimumIdleSize(shiroRedisProperties.getConnectionMinimumidleSize())
                    .setMasterConnectionPoolSize(shiroRedisProperties.getConnectPoolSize())
                    .setTimeout(shiroRedisProperties.getTimeout());
        }else {
            return null;
        }
        //创建redission的客户端，交于spring管理
        RedissonClient client = Redisson.create(config);
        return client;
    }

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
     * 没有使用shiro默认的方式，目的是减少本地化缓存
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
    @Bean(name = "lifecycleBeanPostProcessor")
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
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
        //  自定义过滤器
        shiroFilterFactoryBean.setFilters(customFilterMap());
        //  过滤器链
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap());
        //  设置登录地址
        shiroFilterFactoryBean.setLoginUrl("/login");
        //  设置没有权限跳转的地址
        shiroFilterFactoryBean.setUnauthorizedUrl("/login");
        return shiroFilterFactoryBean;
    }

    private Map<String, Filter> customFilterMap() {
        Map<String, Filter> map = new HashMap<>(1);
        map.put("roles-or", new CustomFilter());
        return map;
    }

    /**
     * 过滤器链，从配置文件中读取 url 和 权限
     *
     * @return map
     */
    private Map<String, String> filterChainDefinitionMap() {
        LinkedProperties properties = PropertiesUtils.readProperties("/authentication.properties");
        List<Object> keyList = properties.getKeyList();
        Map<String, String> map = new LinkedHashMap<>(properties.entrySet().size());

        for (Object obj : keyList) {
            map.put(obj.toString(), properties.get(obj).toString());
        }
        return map;
    }
}
