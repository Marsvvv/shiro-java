package org.shiro.demo.service;

import org.shiro.demo.entity.Resource;
import org.shiro.demo.vo.MenuVo;

import java.util.List;
import java.util.Map;

/**
 * 菜单服务
 *
 * @author asus
 */
public interface IMenuService {
    /**
     * 查询子菜单
     *
     * @param parentId parentId
     * @return List<MenuVo>
     */
    List<MenuVo> findByResourceType(String parentId);

    /**
     * 时间滚动
     *
     * @return Map<String, String>
     */
    Map<String, String> rollingTime();

    /**
     * 查询每个系统的顶级菜单
     *
     * @return List<Resource>
     */
    List<Resource> findTopLevel();

}
