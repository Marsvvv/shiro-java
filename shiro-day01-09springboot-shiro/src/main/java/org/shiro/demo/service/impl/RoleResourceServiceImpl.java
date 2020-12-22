package org.shiro.demo.service.impl;

import org.shiro.demo.entity.RoleResource;
import org.shiro.demo.mapper.RoleResourceMapper;
import org.shiro.demo.service.IRoleResourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色资源表 服务实现类
 * </p>
 *
 * @author yjw
 * @since 2020-12-22
 */
@Service
public class RoleResourceServiceImpl extends ServiceImpl<RoleResourceMapper, RoleResource> implements IRoleResourceService {

}
