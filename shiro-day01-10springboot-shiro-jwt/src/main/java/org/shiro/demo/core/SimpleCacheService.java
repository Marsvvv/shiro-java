package org.shiro.demo.core;

import org.apache.shiro.cache.Cache;

/**
 * 实现缓存管理服务
 *
 * @author Tobu
 */
public interface SimpleCacheService {

    /**
     * 创建缓存
     *
     * @param cacheName cacheName
     * @param cache     cache
     */
    void createCache(String cacheName, Cache<Object, Object> cache);

    /**
     * 获取缓存
     *
     * @param cacheName cacheName
     * @return Cache
     */
    Cache<Object, Object> getCache(String cacheName);

    /**
     * 删除缓存
     *
     * @param cacheName cacheName
     */
    void removeCache(String cacheName);

    /**
     * 修改缓存
     *
     * @param cacheName cacheName
     * @param cache     cache
     */
    void updateCache(String cacheName, Cache<Object, Object> cache);
}
