package org.shiro.demo.core.impl;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.shiro.demo.constant.CacheConstant;
import org.shiro.demo.util.ShiroRedissionSerialize;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;


public class RedisSessionDao extends AbstractSessionDAO {

    @Resource
    RedissonClient redissonClientForShiro;

    @Value("${custom.redis.globalSessionTimeout}")
    private Integer globalSessionTimeout;

    /**
     * 创建session
     *
     * @param session session
     * @return Serializable
     */
    @Override
    protected Serializable doCreate(Session session) {
        //  创建唯一标识 sessionId
        Serializable sessionId = generateSessionId(session);
        //  为session会话指定唯一的sessionId
        assignSessionId(session, sessionId);
        //  放入缓存中
        String key = CacheConstant.GROUP_CAS + sessionId;
        RBucket<String> bucket = redissonClientForShiro.getBucket(key);
        bucket.trySet(ShiroRedissionSerialize.serialize(session), globalSessionTimeout / 1000, TimeUnit.SECONDS);
        return sessionId;
    }

    /**
     * 读取session
     *
     * @param sessionId 唯一标识id
     * @return Session
     */
    @Override
    protected Session doReadSession(Serializable sessionId) {
        String key = CacheConstant.GROUP_CAS + sessionId;
        RBucket<String> bucket = redissonClientForShiro.getBucket(key);
        return (Session) ShiroRedissionSerialize.deserialize(bucket.get());
    }

    /**
     * 更新session
     *
     * @param session session
     * @throws UnknownSessionException UnknownSessionException
     */
    @Override
    public void update(Session session) throws UnknownSessionException {
        String key = CacheConstant.GROUP_CAS + session.getId();
        RBucket<String> bucket = redissonClientForShiro.getBucket(key);
        bucket.set(ShiroRedissionSerialize.serialize(session), globalSessionTimeout / 1000, TimeUnit.SECONDS);
    }

    /**
     * 删除session
     *
     * @param session session
     */
    @Override
    public void delete(Session session) {
        String key = CacheConstant.GROUP_CAS + session.getId();
        RBucket<String> bucket = redissonClientForShiro.getBucket(key);
        bucket.delete();
    }

    /**
     * 统计当前活跃用户数
     *
     * @return Collection<Session>
     */
    @Override
    public Collection<Session> getActiveSessions() {
        // TODO
        return Collections.emptySortedSet();
    }

    public void setGlobalSessionTimeout(Integer globalSessionTimeout) {
        this.globalSessionTimeout = globalSessionTimeout;
    }

    public Integer getGlobalSessionTimeout() {
        return globalSessionTimeout;
    }
}
