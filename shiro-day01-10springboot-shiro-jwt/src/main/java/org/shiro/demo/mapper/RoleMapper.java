package org.shiro.demo.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    /**
     * 分页
     *
     * @param page     page
     * @param roleName roleName
     * @param label    label
     * @return IPage<Role>
     */
    IPage<Role> findRolePage(Page<Role> page, @Param("roleName") String roleName, @Param("label") String label);
}
