package org.shiro.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.shiro.demo.constant.ShiroConstant;
import org.shiro.demo.service.IUserService;
import org.shiro.demo.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tobu
 */
@Slf4j
@Controller
@RequestMapping("/login")
public class LoginController {

    private final IUserService iUserService;

    @Autowired
    public LoginController(IUserService iUserService) {
        this.iUserService = iUserService;
    }

    @GetMapping("")
    public ModelAndView login() {
        return new ModelAndView("/account/login");
    }

    @PostMapping("/usersLongin")
    public ModelAndView usersLongin(LoginVo loginVo) {
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

    @GetMapping("/usersLongout")
    public String usersLongout() {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            subject.logout();
        }
        return "/account/login";
    }

    /**
     * 编辑密码
     */
    @RequestMapping(value = "/editorpassword")
    public ModelAndView editorPassword() {
        return new ModelAndView("/user/user-editor-password");
    }

    /**
     * 保存新密码
     */
    @RequestMapping(value = "/saveNewPassword")
    @ResponseBody
    public Boolean saveNewPassword(String oldPassword, String plainPassword) {
        return iUserService.saveNewPassword(oldPassword, plainPassword);
    }
}
