package org.shiro.demo.core.impl;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 * 自定义密码比较器，并且自定义密码重试次数
 * 密码重试原理是将用户id+密码重试次数存入redis中
 *
 * @author Tobu
 */
public class RetryLimitCredentialsMatcher extends HashedCredentialsMatcher {

    private RedissonClient redissonClient;

    private static final Long RETRY_LIMIT_NUM = 4L;

    public RetryLimitCredentialsMatcher(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        //  获取登录名
        String loginName = (String) token.getPrincipal();

        //  获取是否有登录缓存,如果没有就创建一个缓存 (用户名+登录次数) 次数从0开始
        RAtomicLong atomicLong = redissonClient.getAtomicLong(loginName);
        long count = atomicLong.get();

        //  如果超过限制，则驳回本次登录请求
        if (RETRY_LIMIT_NUM <= count) {
            atomicLong.expire(10, TimeUnit.MINUTES);
            throw new ExcessiveAttemptsException("密码连续错误五次，请10分钟后重试");
        }

        //  如果没有超过限制，将登录次数累加1
        atomicLong.incrementAndGet();
        atomicLong.expire(10, TimeUnit.MINUTES);

        //  验证用户名密码是否正确
        boolean result = super.doCredentialsMatch(token, info);
        if (result) {
            atomicLong.delete();
        }
        return result;
    }
}
