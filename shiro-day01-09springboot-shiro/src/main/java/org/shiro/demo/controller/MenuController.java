package org.shiro.demo.controller;

import org.shiro.demo.core.base.ShiroUser;
import org.shiro.demo.entity.Resource;
import org.shiro.demo.service.IMenuService;
import org.shiro.demo.util.ShiroUserUtil;
import org.shiro.demo.vo.MenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 菜单首页管理
 */
@Controller
@RequestMapping(value = "/menus")
public class MenuController {

    @Autowired
    private IMenuService iMenuService;

    /**
     * 加载系统
     *
     * @return ModelAndView
     */
    @RequestMapping(value = "/system")
    public ModelAndView system() {
        ModelAndView modelAndVie = new ModelAndView("/menus/menus-system");
        Map<String, Object> map = new HashMap<String, Object>();
        ShiroUser shiroUser = ShiroUserUtil.getShiroUser();
        map.put("currentUser", shiroUser);
        List<Resource> list = iMenuService.findTopLevel();
        map.put("list", list);
        map.putAll(iMenuService.rollingTime());
        modelAndVie.addAllObjects(map);
        return modelAndVie;
    }

    /**
     * 首页信息
     */
    @RequestMapping(value = "/home")
    public ModelAndView home() {
        return new ModelAndView("/menus/menus-home");
    }

    /**
     * 获得所有菜单
     */
    @RequestMapping(value = "/findAllMenu")
    @ResponseBody
    public List<MenuVo> findAllMenu(@RequestParam("id") String id) {
        return iMenuService.findByResourceType(id);
    }
}
