package org.shiro.demo.controller;


import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.shiro.demo.constant.ShiroConstant;
import org.shiro.demo.service.IUserService;
import org.shiro.demo.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

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
@RequestMapping("/login")
public class UserController {

    @Autowired
    private IUserService iUserService;

    @GetMapping("/")
    public ModelAndView loginPage() {
        return new ModelAndView("/account/login");
    }

    @PostMapping("/usersLongin")
    public ModelAndView login(LoginVo loginVo) {
        ModelAndView modelAndView = new ModelAndView("/account/login");
        String shiroLoginFailure = null;
        Map<String, String> map = new HashMap<String, String>();
        try {
            loginVo.setSystemCode(ShiroConstant.PLATFORM_MGT);
            iUserService.login(loginVo);
        } catch (UnknownAccountException ex) {
            log.error("登陆异常:{}", ex);
            shiroLoginFailure = "登录失败 - 账号不存在！";
            map.put("loginName", loginVo.getLoginName());
            map.put("shiroLoginFailure", shiroLoginFailure);
            modelAndView.addAllObjects(map);
        } catch (IncorrectCredentialsException ex) {
            log.error("登陆异常:{}", ex);
            shiroLoginFailure = "登录失败 - 密码不正确！";
            map.put("shiroLoginFailure", shiroLoginFailure);
            map.put("loginName", loginVo.getLoginName());
            modelAndView.addAllObjects(map);
        } catch (Exception ex) {
            log.error("登陆异常:{}", ex);
            shiroLoginFailure = "登录失败 - 请联系平台管理员！";
            map.put("shiroLoginFailure", shiroLoginFailure);
            map.put("loginName", loginVo.getLoginName());
            modelAndView.addAllObjects(map);
        }
        if (shiroLoginFailure != null) {
            return modelAndView;
        }
        modelAndView.setViewName("redirect:/menus/system");
        return modelAndView;
    }
}
