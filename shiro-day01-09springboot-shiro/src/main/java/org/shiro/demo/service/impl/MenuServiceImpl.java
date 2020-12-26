package org.shiro.demo.service.impl;

import org.shiro.demo.constant.ResourceConstant;
import org.shiro.demo.constant.SuperConstant;
import org.shiro.demo.entity.Resource;
import org.shiro.demo.mapper.MenuMapper;
import org.shiro.demo.service.IMenuService;
import org.shiro.demo.util.CnCalendarUtil;
import org.shiro.demo.util.ShiroUserUtil;
import org.shiro.demo.vo.MenuVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class MenuServiceImpl implements IMenuService {

    @Value("${myshiro.resource.systemcode}")
    private String systemCode;

    @javax.annotation.Resource
    private MenuMapper menuMapper;

    @Override
    public List<Resource> findTopLevel() {
        return menuMapper.findTopLevel(ShiroUserUtil.getShiroUser().getResourceIds(), ResourceConstant.MENU
                , SuperConstant.YES, SuperConstant.YES, systemCode);
    }

    @Override
    public List<MenuVo> findByResourceType(String parentId) {
        //查询二级菜单
        Map<String, Object> values = new HashMap<String, Object>();
        values.put("parentId", parentId);
        values.put("resourceType", ResourceConstant.MENU);
        values.put("resourceIdList", ShiroUserUtil.getShiroUser().getResourceIds());
        values.put("systemCode", systemCode);
        values.put("enableFlag", SuperConstant.YES);
        List<MenuVo> list = menuMapper.findByResourceType(ShiroUserUtil.getShiroUser().getResourceIds(), ResourceConstant.MENU,
                parentId, SuperConstant.YES, systemCode);
        for (int i = 0; i < list.size(); i++) {
            values.put("parentId", list.get(i).getMenuid());
            List<MenuVo> iterableChildren = menuMapper.findByResourceType(ShiroUserUtil.getShiroUser().getResourceIds(), ResourceConstant.MENU,
                    list.get(i).getMenuid(), SuperConstant.YES, systemCode);
            list.get(i).setMenus(iterableChildren);
        }
        return list;
    }

    @Override
    public Map<String, String> rollingTime() {
        Map<String, String> map = new HashMap<String, String>();
        String today = null;
        String lunar = null;
        String hourMinute = null;
        CnCalendarUtil cnCalendar = new CnCalendarUtil();
        lunar = cnCalendar.getNongli(new Date());
        String[] week = new String[]{"", "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        int weekNum = calendar.get(Calendar.DAY_OF_WEEK);
        today = "今天是: " + sdf.format(calendar.getTime()) + "（农历：" + lunar + "） " + week[weekNum];
        sdf = new SimpleDateFormat("HH:mm:ss");
        hourMinute = sdf.format(calendar.getTime());
        map.put("today", today);
        map.put("hourMinute", hourMinute);
        return map;
    }
}
