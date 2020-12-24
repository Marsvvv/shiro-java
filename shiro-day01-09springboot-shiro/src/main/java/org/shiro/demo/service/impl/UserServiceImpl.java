package org.shiro.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.shiro.demo.core.base.SimpleToken;
import org.shiro.demo.entity.User;
import org.shiro.demo.mapper.UserMapper;
import org.shiro.demo.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.shiro.demo.util.ShiroUtil;
import org.shiro.demo.vo.LoginVo;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author yjw
 * @since 2020-12-22
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public User findUserByLoginName(String loginName) {
        List<User> userList = list(new LambdaQueryWrapper<User>()
                .eq(User::getLoginName, loginName));
        if (userList.size() >= 1) {
            return userList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Map<String, String> login(LoginVo loginVo) {
        Map<String, String> map = new HashMap<>();
        try {
            SimpleToken token = new SimpleToken(null, loginVo.getLoginName(), loginVo.getPassWord());
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
        } catch (UnknownAccountException ex) {
            log.error("登陆异常:{}", ex);
            throw new UnknownAccountException(ex);
        } catch (IncorrectCredentialsException ex) {
            log.error("登陆异常:{}", ex);
            throw new IncorrectCredentialsException(ex);
        }
        //  根据session获取sessionId
        map.put("authorizationKey", ShiroUtil.getShiroSessionId());
        return map;
    }
}
