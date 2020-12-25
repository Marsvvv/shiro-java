package org.shiro.demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.shiro.demo.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.shiro.demo.vo.LoginVo;
import org.shiro.demo.vo.UserVo;

import java.util.List;
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

    /**
     * 修改密码
     *
     * @param oldPwd 老密码
     * @param newPwd 新密码
     * @return 是否成功
     */
    Boolean saveNewPassword(String oldPwd, String newPwd);

    /**
     * 分页查询
     *
     * @param userVo userVo
     * @param rows   rows
     * @param page   page
     * @return IPage<User>
     */
    IPage<User> findUserList(UserVo userVo, Integer rows, Integer page);

    /**
     * 新增,修改对象
     *
     * @param userVo userVo
     * @return Boolean
     */
    Boolean saveOrUpdateUser(UserVo userVo);

    /**
     * 密码加密
     *
     * @param userVo userVo
     */
    void entryptPassword(UserVo userVo);

    /**
     * 验证用户是否存在
     *
     * @param loginName loginName
     * @return Boolean
     */
    Boolean getUserByLoginNameOrMobilOrEmail(String loginName);

    /**
     * 用户拥有的角色
     *
     * @param id id
     * @return List<String>
     */
    List<String> findUserHasRoleIds(String id);
}
