package org.shiro.demo.service;

import org.shiro.demo.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.shiro.demo.vo.LoginVo;

import java.util.Map;

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

    /**
     * 登录操作
     *
     * @param loginVo loginVo
     */
    Map<String, String> login(LoginVo loginVo);
}
