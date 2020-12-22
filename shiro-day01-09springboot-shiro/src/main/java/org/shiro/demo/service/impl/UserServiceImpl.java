package org.shiro.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.shiro.demo.entity.User;
import org.shiro.demo.mapper.UserMapper;
import org.shiro.demo.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
