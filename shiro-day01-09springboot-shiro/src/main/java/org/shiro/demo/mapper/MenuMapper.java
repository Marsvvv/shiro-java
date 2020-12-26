package org.shiro.demo.mapper;

import org.apache.ibatis.annotations.Param;
import org.shiro.demo.entity.Resource;
import org.shiro.demo.vo.MenuVo;

import java.util.List;
import java.util.Map;

/**
 * 菜单服务器层 Mapper
 *
 * @author asus
 */
public interface MenuMapper {

    /**
     * 查询每个系统的顶级菜单
     *
     * @param map map
     * @return List<Resource>
     */
    List<Resource> findTopLevel(@Param("resourceIdList") List<String> resourceIdList, @Param("resourceType") String resourceType,
                                @Param("isSystemRoot") String isSystemRoot, @Param("enableFlag") String enableFlag,
                                @Param("systemCode") String systemCode);

    /**
     * 查询子菜单
     *
     * @param map map
     * @return List<MenuVo>
     */
    List<MenuVo> findByResourceType(@Param("resourceIdList") List<String> resourceIdList, @Param("resourceType") String resourceType,
                                    @Param("parentId") String parentId, @Param("enableFlag") String enableFlag,
                                    @Param("systemCode") String systemCode);
}
