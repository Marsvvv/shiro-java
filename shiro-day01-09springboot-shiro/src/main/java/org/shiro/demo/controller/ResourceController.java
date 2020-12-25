package org.shiro.demo.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.shiro.demo.constant.SuperConstant;
import org.shiro.demo.entity.Resource;
import org.shiro.demo.service.IResourceService;
import org.shiro.demo.vo.TreeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 资源表 前端控制器
 * </p>
 *
 * @author yjw
 * @since 2020-12-22
 */
@Slf4j
@RestController
@RequestMapping("/resource")
public class ResourceController {

    @Autowired
    private IResourceService iResourceService;

    @InitBinder
    protected void initBinder(ServletRequestDataBinder binder) {
        // 去掉空格
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
    }

    /**
     * 初始化列表
     */
    @RequestMapping(value = "/listInitialize")
    public String listInitialize() {
        return "/resource/resource-listInitialize";
    }

    /**
     * 分页列表
     *
     * @param resource resource
     * @param rows     rows
     * @param page     page
     * @return ModelMap
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public ModelMap list(Resource resource, Integer rows, Integer page) {
        IPage<Resource> iPage = iResourceService.findResourceList(resource, rows, page);
        ModelMap modelMap = new ModelMap();
        //数据表格数据传递
        modelMap.put("rows", iPage.getRecords());
        //统计条数传递
        modelMap.put("total", iPage.getTotal());
        return modelMap;
    }

    /**
     * 新增页面
     *
     * @param resource resource
     * @return ModelAndView
     */
    @RequestMapping(value = "/input")
    public ModelAndView input(Resource resource) {
        ModelAndView modelAndView = new ModelAndView("/resource/resource-input");
        Resource parentResource;
        if (resource.getId() == null) {
            parentResource = iResourceService.getById(resource.getParentId());
            if (!parentResource.getParentId().equals(SuperConstant.ROOT_PARENT_ID)) {
                resource.setSystemCode(parentResource.getSystemCode());
            }
        } else {
            resource = iResourceService.getById(resource.getId());
            parentResource = iResourceService.getById(resource.getParentId());
        }
        modelAndView.addObject("parentId", resource.getParentId());
        modelAndView.addObject("parentName", parentResource.getResourceName());
        modelAndView.addObject("resource", resource);

        return modelAndView;
    }

    /**
     * 新增修改
     */
    @RequestMapping(value = "/save")
    @ResponseBody
    public boolean save(@ModelAttribute("resource") Resource resource) {
        boolean flag = true;
        try {
            iResourceService.saveOrUpdateResource(resource);
        } catch (Exception e) {
            log.error("保存资源出错:{}", e);
            flag = false;
        }
        return flag;
    }

    /**
     * 删除资源
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public String delete(@RequestParam("ids") String ids) throws SecurityException {
        String[] idsStrings = ids.split(",");
        List<String> list = new ArrayList<>(Arrays.asList(idsStrings));
        iResourceService.removeByIds(list);
        return "success";
    }

    /**
     * 资源标识是否重复
     */
    @RequestMapping(value = "/checkLabel")
    @ResponseBody
    public String checkLabel(@RequestParam("label") String label,
                             @RequestParam("oldLabel") String oldLabel) {
        if (label.equals(oldLabel)) {
            return "pass";
        } else if (StrUtil.isEmpty(iResourceService.findByLabel(label))) {
            return "pass";
        }
        return "noPass";
    }

    /**
     * 树形页面
     */
    @RequestMapping(value = "/listTree")
    public ModelAndView listTree() {
        return new ModelAndView("/resource/resource-listTree");
    }

    /**
     * 树形json
     */
    @RequestMapping(value = "/tree")
    @ResponseBody
    public List<TreeVo> tree(String parentId) {
        return iResourceService.findResourceTreeVoByParentId(parentId);
    }

    /**
     * 角色分配资源树形json
     */
    @RequestMapping(value = "/roleResourceTree")
    @ResponseBody
    public List<TreeVo> roleResourceTree(@RequestParam(value = "hasResourceIds") String hasResourceIds) {
        List<TreeVo> list;
        if (StrUtil.isEmpty(hasResourceIds)) {
            list = iResourceService.findAllOrderBySortNoAsc();
        } else {
            list = iResourceService.findAllOrderBySortNoAscChecked(hasResourceIds);
        }
        return list;
    }


    @ModelAttribute("resource")
    public Resource getResourcesById(@RequestParam(value = "id", required = false) String id) {
        if (!StrUtil.isEmpty(id)) {
            return iResourceService.getById(id);
        }
        return new Resource();
    }
}
