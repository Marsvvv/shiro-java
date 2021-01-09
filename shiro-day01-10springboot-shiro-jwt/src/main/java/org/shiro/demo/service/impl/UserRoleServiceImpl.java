package org.shiro.demo.service.impl;

import org.shiro.demo.entity.UserRole;
import org.shiro.demo.mapper.UserRoleMapper;
import org.shiro.demo.service.IUserRoleService;
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
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

}
