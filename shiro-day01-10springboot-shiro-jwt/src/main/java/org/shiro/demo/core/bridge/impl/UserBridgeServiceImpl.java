package org.shiro.demo.core.bridge.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.shiro.demo.constant.CacheConstant;
import org.shiro.demo.core.SimpleCacheService;
import org.shiro.demo.core.base.ShiroUser;
import org.shiro.demo.core.base.SimpleMapCache;
import org.shiro.demo.core.bridge.UserBridgeService;
import org.shiro.demo.entity.Role;
import org.shiro.demo.entity.User;
import org.shiro.demo.mapper.ResourceMapper;
import org.shiro.demo.mapper.RoleMapper;
import org.shiro.demo.service.IUserService;
import org.shiro.demo.util.ShiroUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 桥接接口
 *
 * @author asus
 */
@Service("userBridgeService")
public class UserBridgeServiceImpl implements UserBridgeService {

    @Autowired
    private IUserService userService;

    @Resource
    private ResourceMapper resourceMapper;

    @Resource
    private RoleMapper roleMapper;

    @Autowired
    private SimpleCacheService simpleCacheService;

    @Override
    public User findUserByLoginName(String loginName) {
        String key = CacheConstant.FIND_USER_BY_LOGINNAME + loginName;
        Cache<Object, Object> cache = simpleCacheService.getCache(key);
        if (BeanUtil.isNotEmpty(cache)) {
            return (User) cache.get(key);
        }
        User user = userService.findUserByLoginName(loginName);
        if (BeanUtil.isNotEmpty(user)) {
            Map<Object, Object> map = new HashMap<>(1);
            map.put(key, user);
            SimpleMapCache mapCache = new SimpleMapCache(key, map);
            simpleCacheService.createCache(key, mapCache);
        }
        return user;
    }

    @Override
    public List<String> findResourceIdsByUserId(String userId) {
        String sessionId = ShiroUtil.getShiroSessionId();
        String key = CacheConstant.RESOURCES_KEY_IDS + sessionId;
        Cache<Object, Object> cache = simpleCacheService.getCache(key);
        if (BeanUtil.isNotEmpty(cache)) {
            return (List<String>) cache.get(key);
        }
        List<org.shiro.demo.entity.Resource> resourceList = resourceMapper.findResourcesByUserId(userId);
        if (CollectionUtil.isNotEmpty(resourceList)) {
            Map<Object, Object> map = new HashMap<>(1);
            map.put(key, resourceList);
            SimpleMapCache simpleMapCache = new SimpleMapCache(key, map);
            simpleCacheService.createCache(key, simpleMapCache);
        }
        return resourceList.stream().map(org.shiro.demo.entity.Resource::getId).collect(Collectors.toList());
    }

    @Override
    public AuthorizationInfo getAuthorizationInfo(ShiroUser shiroUser) {
        String sessionId = ShiroUtil.getShiroSessionId();
        String key = CacheConstant.ROLE_KEY + sessionId;
        Cache<Object, Object> cache = simpleCacheService.getCache(key);
        if (BeanUtil.isNotEmpty(cache)) {
            AuthorizationInfo authorizationInfo = (AuthorizationInfo) cache.get(key);
            return authorizationInfo;
        }
        //  查询用户对应的资源角色
        List<Role> roleList = roleMapper.findRoleList(shiroUser.getId());
        List<String> role = roleList.stream().map(Role::getLabel).collect(Collectors.toList());
        //  查询用户对应的资源
        List<org.shiro.demo.entity.Resource> resourceList = resourceMapper.findResourcesByUserId(shiroUser.getId());
        List<String> resource = resourceList.stream().map(org.shiro.demo.entity.Resource::getLabel).collect(Collectors.toList());
        //  构建鉴权信息对象
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRoles(role);
        info.addStringPermissions(resource);
        if (BeanUtil.isNotEmpty(info)) {
            Map<Object, Object> map = new HashMap<>(1);
            map.put(key, info);
            SimpleMapCache simpleMapCache = new SimpleMapCache(key, map);
            simpleCacheService.createCache(key, simpleMapCache);
        }
        return info;
    }
}
