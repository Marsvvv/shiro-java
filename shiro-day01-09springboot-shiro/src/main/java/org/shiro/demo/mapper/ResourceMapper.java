package org.shiro.demo.mapper;

import org.apache.ibatis.annotations.Param;
import org.shiro.demo.entity.Resource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 资源表 Mapper 接口
 * </p>
 *
 * @author yjw
 * @since 2020-12-22
 */
public interface ResourceMapper extends BaseMapper<Resource> {

    /**
     * 根据用户id查询资源列表
     *
     * @param userId 用户id
     * @return 资源列表
     */
    List<Resource> findResourcesByUserId(@Param("userId") String userId);
}
