package org.shiro.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.Serializable;

/**
 * Realm 使用 Redis 缓存，Redission 作为连接工具
 * 此类为 Redission连接配置类
 * 将注入到 ShiroConig 类中
 *
 * @author asus
 */
@Configuration
@ConfigurationProperties(prefix = "custom.redis")
@PropertySource("classpath:application.yml")
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
    private int connectPoolSize;

    /**
     * 最大连接数
     */
    private int connectionMinimumidleSize;

    /**
     * 等待数据返回超时时间
     */
    private int timeout;

    public String getNodes() {
        return nodes;
    }

    public void setNodes(String nodes) {
        this.nodes = nodes;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getConnectPoolSize() {
        return connectPoolSize;
    }

    public void setConnectPoolSize(int connectPoolSize) {
        this.connectPoolSize = connectPoolSize;
    }

    public int getConnectionMinimumidleSize() {
        return connectionMinimumidleSize;
    }

    public void setConnectionMinimumidleSize(int connectionMinimumidleSize) {
        this.connectionMinimumidleSize = connectionMinimumidleSize;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
