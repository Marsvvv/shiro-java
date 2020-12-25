package org.shiro.demo.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    /**
     * 资源多条件查询
     *
     * @param page         page
     * @param parentId     parentId
     * @param resourceName resourceName
     * @param label        label
     * @param requestPath  requestPath
     * @return IPage<Resource>
     */
    IPage<Resource> findResourceList(Page<Resource> page, @Param("parentId") String parentId,
                                     @Param("resourceName") String resourceName, @Param("label") String label,
                                     @Param("requestPath") String requestPath);
}
