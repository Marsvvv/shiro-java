package org.shiro.demo.core.bridge.impl;

import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.shiro.demo.core.base.ShiroUser;
import org.shiro.demo.core.bridge.UserBridgeService;
import org.shiro.demo.entity.Role;
import org.shiro.demo.entity.User;
import org.shiro.demo.mapper.ResourceMapper;
import org.shiro.demo.mapper.RoleMapper;
import org.shiro.demo.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service("userBridgeService")
public class UserBridgeServiceImpl implements UserBridgeService {

    @Autowired
    private IUserService userService;

    @Resource
    private ResourceMapper resourceMapper;

    @Resource
    private RoleMapper roleMapper;

    @Override
    public User findUserByLoginName(String loginName) {
        return userService.findUserByLoginName(loginName);
    }

    @Override
    public List<String> findResourceIdsByUserId(String userId) {
        List<org.shiro.demo.entity.Resource> resourceList = resourceMapper.findResourcesByUserId(userId);
        return resourceList.stream().map(org.shiro.demo.entity.Resource::getId).collect(Collectors.toList());
    }

    @Override
    public AuthorizationInfo getAuthorizationInfo(ShiroUser shiroUser) {
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
        return info;
    }
}
