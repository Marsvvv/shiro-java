package org.shiro.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * Realm 使用 Redis 缓存，Redission 作为连接工具
 * 此类为 Redission连接配置类
 * 将注入到 ShiroConig 类中
 */
@Data
@ConfigurationProperties(prefix = "myshiro.redis")
public class ShiroRedisProperties implements Serializable {

    /**
     * redis连接地址
     */
    private String nodes;

    /**
     * 获取连接超时时间
     */
    private int connectTimeout;

    /**
     * 最小空闲连接数
     */
    private int countPoolSize;

    /**
     * 最大连接数
     */
    private int connectionMaximumidleSize;

    /**
     * 等待数据返回时间
     */
    private int timeout;
}
