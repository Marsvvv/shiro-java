package org.shiro.demo.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.shiro.demo.constant.ShiroConstant;
import org.shiro.demo.constant.SuperConstant;
import org.shiro.demo.entity.User;
import org.shiro.demo.service.IUserService;
import org.shiro.demo.vo.LoginVo;
import org.shiro.demo.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author yjw
 * @since 2020-12-22
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService iUserService;

    /**
     * 初始化列表
     */
    @GetMapping("/listInitialize")
    public String listInitialize() {
        return "/user/user-listInitialize";
    }

    /**
     * list(分页列表)
     */
    @GetMapping(value = "/list")
    @ResponseBody
    public ModelMap list(UserVo userVo, Integer rows, Integer page) {
        IPage<User> iPage = iUserService.findUserList(userVo, rows, page);
        ModelMap modelMap = new ModelMap();
        // 数据表格数据传递
        modelMap.put("rows", iPage.getRecords());
        // 统计条数传递
        modelMap.put("total", iPage.getTotal());
        return modelMap;
    }

    /**
     * 新增, 修改对象
     *
     * @param userVo userVo
     */
    @RequestMapping(value = "/save")
    @ResponseBody
    public Boolean save(@ModelAttribute("user") UserVo userVo) {
        return iUserService.saveOrUpdateUser(userVo);
    }

    /**
     * 验证用户唯一
     *
     * @param loginName    loginName
     * @param oldLoginName oldLoginName
     * @return 传人参数
     */
    @RequestMapping(value = "/checkLoginName")
    @ResponseBody
    public String checkLoginName(@RequestParam(value = "loginName") String loginName, @RequestParam(value = "oldLoginName") String oldLoginName) {
        if (!StrUtil.isEmpty(oldLoginName) && loginName.equals(oldLoginName)) {
            return "pass";
        }
        if (iUserService.getUserByLoginNameOrMobilOrEmail(loginName)) {
            return "pass";
        } else {
            return "fail";
        }
    }

    /**
     * 禁用用户
     *
     * @param ids ids
     */
    @RequestMapping(value = "/start")
    @ResponseBody
    public String start(@RequestParam(value = "ids") String ids) {

        String[] idsStrings = ids.split(",");
        List<String> list = Arrays.asList(idsStrings);
        List<User> userList = iUserService.list(new LambdaQueryWrapper<User>()
                .in(User::getId, list));

        for (User user : userList) {
            user.setEnableFlag(SuperConstant.YES);
        }
        boolean flag = iUserService.updateBatchById(userList);
        if (flag) {
            return "success";
        }
        return "fail";
    }

    /**
     * 启用
     *
     * @param ids ids
     */
    @RequestMapping(value = "/stop")
    @ResponseBody
    public String stop(@RequestParam(value = "ids") String ids) {

        String[] idsStrings = ids.split(",");
        List<String> list = Arrays.asList(idsStrings);
        List<User> userList = iUserService.list(new LambdaQueryWrapper<User>()
                .in(User::getId, list));

        for (User user : userList) {
            user.setEnableFlag(SuperConstant.NO);
        }
        boolean flag = iUserService.updateBatchById(userList);
        if (flag) {
            return "success";
        }
        return "fail";
    }

    /**
     * @param userVo userVo
     * @return ModelAndView
     */
    @RequestMapping(value = "/input")
    public ModelAndView input(@ModelAttribute("user") UserVo userVo) {
        if (!StrUtil.isEmpty(userVo.getId())) {
            List<String> list = iUserService.findUserHasRoleIds(userVo.getId());
            StringBuilder roleIdsBuffer = new StringBuilder(100);
            for (int i = 0; i < list.size(); i++) {
                roleIdsBuffer.append(list.get(i));
                if (i + 1 != list.size()) {
                    roleIdsBuffer.append(",");
                }
            }
            userVo.setHasRoleIds(roleIdsBuffer.toString());
        }
        return new ModelAndView("/user/user-input").addObject("user", userVo);
    }

    /**
     * @param id id
     * @return UserVo
     */
    @ModelAttribute("user")
    public UserVo getUserById(@RequestParam(value = "id", required = false) String id) {

        if (!StrUtil.isEmpty(id)) {
            User user = iUserService.getById(id);
            return BeanUtil.copyProperties(user, UserVo.class);
        }
        return new UserVo();
    }
}
