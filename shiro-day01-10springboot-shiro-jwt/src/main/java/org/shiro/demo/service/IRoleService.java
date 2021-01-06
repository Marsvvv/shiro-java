package org.shiro.demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.shiro.demo.entity.Role;
import org.shiro.demo.vo.ComboboxVo;
import org.shiro.demo.vo.RoleVo;

import java.util.List;

/**
 * <p>
 * 用户角色表 服务类
 * </p>
 *
 * @author yjw
 * @since 2020-12-22
 */
public interface IRoleService extends IService<Role> {

    /**
     * 角色的分页查询
     *
     * @param roleVo roleVo
     * @param rows   rows
     * @param page   page
     * @return List<Role>
     */
    IPage<Role> findRoleList(RoleVo roleVo, Integer rows, Integer page);

    /**
     * 按Id查询角色
     *
     * @param id id
     * @return RoleVo
     */
    RoleVo getRoleById(String id);

    /**
     * 新增或修改
     * @param roleVo roleVo
     * @return boolean
     */
    boolean saveOrUpdateRole(RoleVo roleVo);

    /**
     * 角色删除
     *
     * @param list       list
     * @param enableFlag enableFlag
     * @return Boolean
     */
    Boolean updateByIds(List<String> list, String enableFlag);


    /**
     * 根据角色标志查询角色
     *
     * @param lable lable
     * @return Role
     */
    Role findRoleByLable(String lable);

    /**
     * 查询有效角色下拉列表
     *
     * @param roleIds roleIds
     * @return List<ComboboxVo>
     */
    List<ComboboxVo> findRoleComboboxVo(String roleIds);

    /**
     * 查询角色拥有的资源Id字符串
     *
     * @param id id
     * @return List<String>
     */
    List<String> findRoleHasResourceIds(String id);
}
