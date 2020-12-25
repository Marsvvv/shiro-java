package org.shiro.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.shiro.demo.constant.SuperConstant;
import org.shiro.demo.entity.Role;
import org.shiro.demo.entity.RoleResource;
import org.shiro.demo.mapper.RoleMapper;
import org.shiro.demo.service.IRoleResourceService;
import org.shiro.demo.service.IRoleService;
import org.shiro.demo.vo.ComboboxVo;
import org.shiro.demo.vo.RoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Resource
    private RoleMapper roleMapper;

    @Autowired
    private IRoleResourceService iRoleResourceService;

    @Override
    public IPage<Role> findRoleList(RoleVo roleVo, Integer rows, Integer page) {
        Page<Role> iPage = new Page<>(page, rows);
        return roleMapper.findRolePage(iPage, roleVo.getRoleName(), roleVo.getLabel());
    }

    @Override
    public RoleVo getRoleById(String id) {
        return BeanUtil.copyProperties(getById(id), RoleVo.class);
    }

    @Override
    public boolean saveOrUpdateRole(RoleVo roleVo) {
        Role role = new Role();
        BeanUtil.copyProperties(role, roleVo);
        boolean flag = true;
        try {
            if (StrUtil.isEmpty(roleVo.getId())) {
                role.setEnableFlag(SuperConstant.YES);
                roleMapper.insert(role);
                roleVo.setId(role.getId());
            } else {
                getById(role.getId());
                iRoleResourceService.remove(new LambdaQueryWrapper<RoleResource>()
                        .eq(RoleResource::getRoleId, role.getId()));
            }
            bachRoleResource(roleVo);
        } catch (Exception e) {
            log.error("保存角色出错{}", e);
            flag = false;
        }
        return flag;
    }

    /**
     * <b>方法名：</b>：bachRoleResource<br>
     * <b>功能说明：</b>：批量处理RoleResource中间表<br>
     *
     * @author <font color='blue'>束文奇</font>
     * @date 2017-7-11 下午4:01:12
     */
    private void bachRoleResource(RoleVo roleVo) {
        if (!StrUtil.isEmpty(roleVo.getHasResourceIds())) {
            List<RoleResource> list = new ArrayList<>();
            String[] resourceIdList = roleVo.getHasResourceIds().split(",");
            for (String resourceId : resourceIdList) {
                RoleResource roleResource = new RoleResource();
                roleResource.setRoleId(roleVo.getId());
                roleResource.setResourceId(resourceId);
                roleResource.setEnableFlag(SuperConstant.YES);
                list.add(roleResource);
            }
            iRoleResourceService.saveBatch(list);
        }
    }

    @Override
    public Boolean updateByIds(List<String> list, String enableFlag) {
        List<Role> roles = list(new LambdaQueryWrapper<Role>()
                .in(Role::getId, list));
        for (Role r : roles) {
            r.setEnableFlag(enableFlag);
        }
        return updateBatchById(roles);
    }

    @Override
    public Role findRoleByLable(String lable) {
        List<Role> list = null;
        if (!StrUtil.isEmpty(lable)) {
            list = list(new LambdaQueryWrapper<Role>()
                    .eq(Role::getLabel, lable));
        }
        if (list != null && list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<ComboboxVo> findRoleComboboxVo(String roleIds) {
        List<Role> roleList = list(new LambdaQueryWrapper<Role>()
                .eq(Role::getEnableFlag, SuperConstant.YES));
        List<ComboboxVo> list = new ArrayList<>();
        for (Role role : roleList) {
            ComboboxVo comboboxVo = new ComboboxVo();
            comboboxVo.setId(role.getId());
            comboboxVo.setText(role.getRoleName());
            list.add(comboboxVo);
        }
        if (!StrUtil.isEmpty(roleIds)) {
            String[] ids = roleIds.split(",");
            for (String id : ids) {
                for (ComboboxVo comboboxVo : list) {
                    if (id.equals(comboboxVo.getId())) {
                        comboboxVo.setSelected(true);
                        break;
                    }
                }
            }
        }
        return list;
    }

    @Override
    public List<String> findRoleHasResourceIds(String id) {
        List<RoleResource> list = iRoleResourceService.list(new LambdaQueryWrapper<RoleResource>()
                .eq(RoleResource::getEnableFlag, SuperConstant.YES)
                .eq(RoleResource::getRoleId, id));
        return list.stream().map(RoleResource::getResourceId).collect(Collectors.toList());
    }
}
