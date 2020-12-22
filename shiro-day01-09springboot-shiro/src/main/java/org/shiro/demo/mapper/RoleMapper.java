package org.shiro.demo.mapper;

import org.apache.ibatis.annotations.Param;
import org.shiro.demo.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 用户角色表 Mapper 接口
 * </p>
 *
 * @author yjw
 * @since 2020-12-22
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<Role> findRoleList(@Param("userId") String userId);
}
