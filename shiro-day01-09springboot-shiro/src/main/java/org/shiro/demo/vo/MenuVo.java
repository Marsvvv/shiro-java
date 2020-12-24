package org.shiro.demo.vo;

import lombok.Data;

import java.util.List;

/**
 * 菜单显示类
 *
 * @author asus
 */
@Data
public class MenuVo {

    /**
     * 菜单Id
     **/
    private String menuid;

    /**
     * 菜单图标
     **/
    private String icon;

    /**
     * 菜单名称
     **/
    private String menuname;

    /**
     * 菜单链接
     **/
    private String url;

    /**
     * 菜单子菜单
     **/
    private List<MenuVo> menus;
}
