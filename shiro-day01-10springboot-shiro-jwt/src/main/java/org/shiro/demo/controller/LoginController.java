package org.shiro.demo.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.redisson.api.RedissonClient;
import org.shiro.demo.constant.ShiroConstant;
import org.shiro.demo.core.SimpleCacheService;
import org.shiro.demo.core.base.ShiroUser;
import org.shiro.demo.core.base.SimpleMapCache;
import org.shiro.demo.core.base.SimpleToken;
import org.shiro.demo.core.impl.JwtTokenManager;
import org.shiro.demo.service.IUserService;
import org.shiro.demo.util.ShiroUserUtil;
import org.shiro.demo.vo.LoginVo;
import org.shiro.demo.web.BaseResponse;
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
    private JwtTokenManager jwtTokenManager;

    @Autowired
    private SimpleCacheService simpleCacheService;

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

    @RequestMapping("login-jwt")
    @ResponseBody
    public BaseResponse loginForJwt(@RequestBody LoginVo loginVo) {
        String sign;
        try {
            //  登录操作
            SimpleToken simpleToken = new SimpleToken(null, loginVo.getLoginName(), loginVo.getPassWord());
            Subject subject = SecurityUtils.getSubject();
            subject.login(simpleToken);
            //  颁发令牌
            ShiroUser shiroUser = ShiroUserUtil.getShiroUser();
            String sessionId = ShiroUserUtil.getShiroSessionId();
            sign = jwtTokenManager.sign(sessionId, loginVo.getLoginName(), 1000 * 60 * 10L);
        } catch (AuthenticationException e) {
            return new BaseResponse(ShiroConstant.LOGIN_FAILURE_CODE, ShiroConstant.LOGIN_FAILURE_MESSAGE);
        }
        //  创建缓存
        Map<Object, Object> map = new HashMap<>(1);
        map.put("jwt" + loginVo.getLoginName(), sign);
        SimpleMapCache mapCache = new SimpleMapCache("jwt" + loginVo.getLoginName(), map);
        simpleCacheService.createCache("jwt" + loginVo.getLoginName(), mapCache);

        return new BaseResponse(ShiroConstant.LOGIN_SUCCESS_CODE, ShiroConstant.LOGIN_SUCCESS_MESSAGE, sign);
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
