package org.shiro.demo.service.impl;

import org.shiro.demo.entity.Role;
import org.shiro.demo.mapper.RoleMapper;
import org.shiro.demo.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户角色表 服务实现类
 * </p>
 *
 * @author yjw
 * @since 2020-12-22
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}
