package org.shiro.demo.mapper;

import org.shiro.demo.entity.Resource;
import org.shiro.demo.vo.MenuVo;

import java.util.List;
import java.util.Map;

/**
 * 菜单服务器层 Mapper
 *
 * @author asus
 */
public interface MenusServiceMapper {

    /**
     * 查询每个系统的顶级菜单
     *
     * @param map map
     * @return List<Resource>
     */
    List<Resource> findTopLevel(Map<String, Object> map);

    /**
     * 查询子菜单
     *
     * @param map map
     * @return List<MenuVo>
     */
    List<MenuVo> findByResourceType(Map<String, Object> map);
}
