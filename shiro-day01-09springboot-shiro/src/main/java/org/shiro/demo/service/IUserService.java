package org.shiro.demo.service;

import org.shiro.demo.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author yjw
 * @since 2020-12-22
 */
public interface IUserService extends IService<User> {

    /**
     * 根据用户名查询用户信息
     *
     * @param loginName 用户名
     * @return 用户信息
     */
    User findUserByLoginName(String loginName);
}
