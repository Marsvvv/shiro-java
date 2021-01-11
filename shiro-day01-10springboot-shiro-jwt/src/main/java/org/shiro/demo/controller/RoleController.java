package org.shiro.demo.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.shiro.demo.constant.SuperConstant;
import org.shiro.demo.entity.Role;
import org.shiro.demo.service.IRoleService;
import org.shiro.demo.vo.ComboboxVo;
import org.shiro.demo.vo.RoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 用户角色表 前端控制器
 * </p>
 *
 * @author yjw
 * @since 2020-12-22
 */
@Controller
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private IRoleService iRoleService;

    /**
     * 跳转到角色的初始化页面
     */
    @RequestMapping(value = "listInitialize")
    @RequiresRoles(value = {"SuperAdmin", "dev"}, logical = Logical.OR)
    public ModelAndView listInitialize() {
        return new ModelAndView("/role/role-listInitialize");
    }

    /**
     * 角色的分页查询
     *
     * @param roleVo 角色对象
     * @param rows   分页个数
     * @param page   分页对象
     * @return ModelMap
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public ModelMap list(RoleVo roleVo, @RequestParam(defaultValue = "5") Integer rows, @RequestParam(defaultValue = "1") Integer page) {
        IPage<Role> iPage = iRoleService.findRoleList(roleVo, rows, page);
        ModelMap modelMap = new ModelMap();
        modelMap.put("rows", iPage.getRecords());
        modelMap.put("total", iPage.getTotal());
        return modelMap;
    }

    /**
     * 跳转到添加和编辑页面
     *
     * @return ModelAndView
     */
    @RequestMapping(value = "/input")
    public ModelAndView input(@ModelAttribute("role") RoleVo roleVo) {
        if (!StrUtil.isEmpty(roleVo.getId())) {
            List<String> resourceIdList = iRoleService.findRoleHasResourceIds(roleVo.getId());
            StringBuilder resourceIdsBuffer = new StringBuilder(100);
            for (int i = 0; i < resourceIdList.size(); i++) {
                resourceIdsBuffer.append(resourceIdList.get(i));
                if (i + 1 != resourceIdList.size()) {
                    resourceIdsBuffer.append(",");
                }
            }
            roleVo.setHasResourceIds(resourceIdsBuffer.toString());
        }
        return new ModelAndView("/role/role-input").addObject("role", roleVo);
    }

    /**
     * 角色保存
     *
     * @param roleVo 角色对象
     * @return true：保存成功，false:保存失败
     */
    @RequestMapping(value = "save")
    @ResponseBody
    public boolean save(@ModelAttribute("role") RoleVo roleVo) throws IllegalAccessException, InvocationTargetException {
        return iRoleService.saveOrUpdateRole(roleVo);
    }


    /**
     * 启用
     *
     * @param ids 角色id拼装的字符串
     * @return success:保存成功
     */
    @RequestMapping(value = "start")
    @ResponseBody
    public String start(String ids) {

        String[] idArray = ids.split(",");
        List<String> list = new ArrayList<>();
        Collections.addAll(list, idArray);
        Boolean flag = iRoleService.updateByIds(list, SuperConstant.YES);
        if (flag) {
            return "success";
        }
        return "fail";
    }

    /**
     * 禁用
     *
     * @param ids 角色id拼装的字符串
     * @return success:保存成功
     */
    @RequestMapping(value = "stop")
    @ResponseBody
    public String stop(String ids) {

        String[] idArray = ids.split(",");
        List<String> list = new ArrayList<>();
        Collections.addAll(list, idArray);
        Boolean flag = iRoleService.updateByIds(list, SuperConstant.NO);
        if (flag) {
            return "success";
        }
        return "fail";
    }

    /**
     * 验证角色标志是否重复
     *
     * @param label    当前输入框的值
     * @param oldLabel 原始值
     * @return pass:不重复，noPass：重复
     */
    @RequestMapping(value = "/checkLabel")
    @ResponseBody
    public String checkLabel(@RequestParam("label") String label, @RequestParam("oldLabel") String oldLabel) {
        if (label.equals(oldLabel)) {
            return "pass";
        } else if (iRoleService.findRoleByLable(label) == null) {
            return "pass";
        }
        return "fail";
    }


    /**
     * 角色下拉框
     *
     * @param roleIds 已选角色ids
     * @return List<ComboboxVo>
     */
    @RequestMapping(value = "/findRoleComboboxVo")
    @ResponseBody
    public List<ComboboxVo> findRoleComboboxVo(@RequestParam("roleIds") String roleIds) {
        return iRoleService.findRoleComboboxVo(roleIds);
    }

    /**
     * 调用该控制器所有方法之前会调用该方法
     *
     * @param id 主键
     */
    @ModelAttribute("role")
    public RoleVo getRoleById(@RequestParam(value = "id", required = false) String id) {
        if (!StrUtil.isEmpty(id)) {
            return iRoleService.getRoleById(id);
        }
        return new RoleVo();
    }
}
