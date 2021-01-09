package org.shiro.demo.core.filter;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.ExpiredSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;
import org.shiro.demo.constant.CacheConstant;
import org.shiro.demo.core.impl.RedisSessionDao;
import org.shiro.demo.util.ShiroUserUtil;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 单点登录过滤器
 *
 * @author asus
 */
@Slf4j
public class SingletonLoginFilter extends AccessControlFilter {

    private RedissonClient redissonClient;

    private RedisSessionDao redisSessionDao;

    private DefaultWebSecurityManager defaultWebSecurityManager;

    public SingletonLoginFilter(RedissonClient redissonClient, RedisSessionDao redisSessionDao, DefaultWebSecurityManager defaultWebSecurityManager) {
        this.redissonClient = redissonClient;
        this.redisSessionDao = redisSessionDao;
        this.defaultWebSecurityManager = defaultWebSecurityManager;
    }

    /**
     * 是否拒绝访问
     *
     * @param servletRequest  servletRequest
     * @param servletResponse servletResponse
     * @param o               o
     * @return boolean
     * @throws Exception Exception
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        return false;
    }

    /**
     * 拒绝访问后的业务逻辑
     *
     * @param servletRequest  servletRequest
     * @param servletResponse servletResponse
     * @return boolean
     * @throws Exception Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        //  判断用户是否登录
        Subject subject = getSubject(servletRequest, servletResponse);
        if (!subject.isAuthenticated()) {
            return false;
        }

        //  使用Redisson创建队列
        String loginName = ShiroUserUtil.getShiroUser().getLoginName();
        RQueue<Object> queue = redissonClient.getQueue(CacheConstant.GROUP_CAS + "SingletonLoginFilter:" + loginName);
        //  获取当前用户sessionId
        String sessionId = ShiroUserUtil.getShiroSessionId();
        //  判断sessionId是否存在在队列中
        boolean contains = queue.contains(sessionId);
        //  不存在则存入当前队列中
        if (!contains) {
            queue.add(sessionId);
        }
        //  判断当前队列是否超过此账号的最大可在线人数
        if (queue.size() > 1) {
            //  获取队列头部数据
            String element = (String) queue.element();
            //  删除Redis 队列头部数据
            queue.remove(element);

            Session session = null;
            try {
                session = defaultWebSecurityManager.getSession(new DefaultSessionKey(element));
            } catch (UnknownSessionException e) {
                log.error("session已经失效");
            } catch (ExpiredSessionException e) {
                log.error("session已经过期");
            }
            if (BeanUtil.isNotEmpty(session)) {
                //  删除Redis 缓存session会话数据
                redisSessionDao.delete(session);
            }
        }

        //  未超过
        return true;
    }
}
