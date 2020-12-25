package org.shiro.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.shiro.demo.constant.SuperConstant;
import org.shiro.demo.core.base.ShiroUser;
import org.shiro.demo.core.base.SimpleToken;
import org.shiro.demo.entity.User;
import org.shiro.demo.entity.UserRole;
import org.shiro.demo.mapper.UserMapper;
import org.shiro.demo.service.IUserRoleService;
import org.shiro.demo.service.IUserService;
import org.shiro.demo.util.DigestUtil;
import org.shiro.demo.util.ShiroUtil;
import org.shiro.demo.vo.LoginVo;
import org.shiro.demo.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

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

    @Resource
    private UserMapper userMapper;

    @Autowired
    private IUserRoleService iUserRoleService;

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
        Map<String, String> map = new HashMap<>(1);
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

    @Override
    public Boolean saveNewPassword(String oldPwd, String newPwd) {
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        User user = getById(shiroUser.getId());
        newPwd = DigestUtil.sha1(newPwd, user.getSalt());
        if (!user.getPassWord().equals(newPwd)) {
            return false;
        }
        user.setPassWord(newPwd);
        try {
            return updateById(user);
        } catch (Exception e) {
            log.error("更新用户密码失败：{}", e);
            return false;
        }
    }

    @Override
    public IPage<User> findUserList(UserVo userVo, Integer rows, Integer page) {
        Page<User> iPage = new Page<>(page, rows);
        return userMapper.findUserList(iPage, userVo.getLoginName(), userVo.getRealName(), userVo.getEmail());
    }

    @Override
    public Boolean saveOrUpdateUser(UserVo userVo) {
        boolean flag = true;
        try {
            if (!StrUtil.isEmpty(userVo.getPlainPassword())) {
                entryptPassword(userVo);
            }
            User user = new User();
            BeanUtil.copyProperties(userVo, user);
            if (StrUtil.isEmpty(userVo.getId())) {
                user.setEnableFlag(SuperConstant.YES);
                save(user);
                userVo.setId(user.getId());
            } else {
                updateById(user);
                iUserRoleService.remove(new LambdaQueryWrapper<UserRole>()
                        .eq(UserRole::getUserId, user.getId()));
            }
            bachUserRole(userVo);
        } catch (Exception e) {
            log.error("保存用户出错{}", e);
            flag = false;
        }
        return flag;
    }

    /**
     * <b>方法名：</b>：bachUserRole<br>
     * <b>功能说明：</b>：批量处理UserRole中间表<br>
     *
     * @author <font color='blue'>束文奇</font>
     * @date 2017-7-11 下午4:01:12
     */
    private void bachUserRole(UserVo userVo) {
        if (!StrUtil.isEmpty(userVo.getHasRoleIds())) {
            List<UserRole> list = new ArrayList<>();
            String[] roleIdList = userVo.getHasRoleIds().split(",");
            for (String roleId : roleIdList) {
                UserRole userRole = new UserRole();
                userRole.setUserId(userVo.getId());
                userRole.setRoleId(roleId);
                userRole.setEnableFlag(SuperConstant.YES);
                list.add(userRole);
            }
            iUserRoleService.saveBatch(list);
        }
    }

    @Override
    public void entryptPassword(UserVo userVo) {
        Map<String, String> map = DigestUtil.entrptPassword(userVo.getPlainPassword());
        userVo.setSalt(map.get("salt"));
        userVo.setPassWord(map.get("password"));
    }

    @Override
    public Boolean getUserByLoginNameOrMobilOrEmail(String loginName) {
        User user = userMapper.getUserIdByLoginNameOrMobilOrEmail(loginName, null, null);
        return BeanUtil.isEmpty(user);
    }

    @Override
    public List<String> findUserHasRoleIds(String id) {
        List<UserRole> userRoleList = iUserRoleService.list(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getRoleId, id)
                .eq(UserRole::getEnableFlag, SuperConstant.YES));
        List<String> list = new ArrayList<>();
        userRoleList.forEach(n -> list.add(n.getRoleId()));
        return list;
    }
}
