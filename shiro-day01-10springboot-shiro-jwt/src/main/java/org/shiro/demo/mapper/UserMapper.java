package org.shiro.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.shiro.demo.entity.User;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author yjw
 * @since 2020-12-22
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 分页
     *
     * @param page      page
     * @param loginName loginName
     * @param realName  realName
     * @param email     email
     * @return IPage<User>
     */
    IPage<User> findUserList(Page<User> page, @Param("loginName") String loginName,
                             @Param("realName") String realName, @Param("email") String email);

    /**
     * 查询用户
     *
     * @param loginName loginName
     * @param realName  realName
     * @param email     email
     * @return User
     */
    User getUserIdByLoginNameOrMobilOrEmail(@Param("loginName") String loginName,
                                            @Param("realName") String realName, @Param("email") String email);
}
