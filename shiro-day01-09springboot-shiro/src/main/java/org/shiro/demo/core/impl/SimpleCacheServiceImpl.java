package org.shiro.demo.core.impl;

import org.apache.shiro.cache.Cache;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.shiro.demo.core.SimpleCacheService;
import org.shiro.demo.util.ShiroRedissionSerialize;
import org.shiro.demo.util.ShiroUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 * @author Tobu
 */
public class SimpleCacheServiceImpl implements SimpleCacheService {

    private final RedissonClient redissonClient;

    @Autowired
    public SimpleCacheServiceImpl(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public void createCache(String cacheName, Cache<Object, Object> cache) {
        RBucket<Object> bucket = redissonClient.getBucket(cacheName);
        bucket.trySet(ShiroRedissionSerialize.serialize(cache), ShiroUtil.getShiroSession().getTimeout() / 1000, TimeUnit.SECONDS);
    }

    @Override
    public Cache<Object, Object> getCache(String cacheName) {
        RBucket<Object> bucket = redissonClient.getBucket(cacheName);
        return (Cache<Object, Object>) bucket.get();
    }

    @Override
    public void removeCache(String cacheName) {
        RBucket<Object> bucket = redissonClient.getBucket(cacheName);
        bucket.delete();
    }

    @Override
    public void updateCache(String cacheName, Cache<Object, Object> cache) {
        RBucket<Object> bucket = redissonClient.getBucket(cacheName);
        bucket.set(ShiroRedissionSerialize.serialize(cache), ShiroUtil.getShiroSession().getTimeout() / 1000, TimeUnit.SECONDS);
    }
}
