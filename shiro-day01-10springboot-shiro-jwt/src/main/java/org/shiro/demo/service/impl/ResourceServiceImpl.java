package org.shiro.demo.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.shiro.demo.constant.SuperConstant;
import org.shiro.demo.entity.Resource;
import org.shiro.demo.mapper.ResourceMapper;
import org.shiro.demo.service.IResourceService;
import org.shiro.demo.vo.TreeVo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 资源表 服务实现类
 * </p>
 *
 * @author yjw
 * @since 2020-12-22
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements IResourceService {

    @javax.annotation.Resource
    private ResourceMapper resourceMapper;

    @Override
    public IPage<Resource> findResourceList(Resource resource, Integer rows, Integer page) {
        Page<Resource> ipage = new Page<>(page, rows);
        return resourceMapper.findResourceList(ipage, resource.getParentId(), resource.getResourceName(), resource.getLabel(), resource.getRequestPath());
    }

    @Override
    public void saveOrUpdateResource(Resource resource) {
        if (!StrUtil.isEmpty(resource.getId())) {
            updateById(resource);
        } else {
            if (StrUtil.isEmpty(resource.getIsSystemRoot())) {
                resource.setIsSystemRoot(SuperConstant.NO);
            }
            resource.setEnableFlag(SuperConstant.YES);
            save(resource);
        }
    }

    @Override
    public String findByLabel(String label) {
        List<Resource> list = list(new LambdaQueryWrapper<Resource>()
                .eq(Resource::getLabel, label)
                .eq(Resource::getEnableFlag, true));
        if (list.size() > 0) {
            return list.get(0).getLabel();
        } else {
            return "";
        }
    }

    @Override
    public List<TreeVo> findResourceTreeVoByParentId(String parentId) {
        List<Resource> list = new ArrayList<>();
        if (SuperConstant.ROOT_PARENT_ID.equals(parentId)) {
            list.addAll(list(new LambdaQueryWrapper<Resource>()
                    .eq(Resource::getIsSystemRoot, SuperConstant.YES)
                    .eq(Resource::getEnableFlag, SuperConstant.YES)));
        } else {
            list.addAll(list(new LambdaQueryWrapper<Resource>()
                    .eq(Resource::getEnableFlag, SuperConstant.YES)
                    .eq(Resource::getParentId, parentId)
                    .orderBy(true, true, Resource::getSortNo)));
        }
        List<TreeVo> treeVoList = new ArrayList<>();
        for (Resource resources : list) {
            TreeVo treeVo = new TreeVo(resources.getId(), resources.getParentId(), resources.getResourceName());
            if (SuperConstant.ROOT_PARENT_ID.equals(parentId)) {
                treeVo.setOpen(Boolean.TRUE);
            }
            treeVoList.add(treeVo);
        }
        return treeVoList;
    }

    @Override
    public List<TreeVo> findAllOrderBySortNoAsc() {
        List<Resource> list = list(new LambdaQueryWrapper<Resource>()
                .eq(Resource::getEnableFlag, SuperConstant.YES)
                .orderByAsc(Resource::getSortNo));
        List<TreeVo> listHandle = new ArrayList<>();
        for (Resource resource : list) {
            TreeVo treeVo = new TreeVo();
            treeVo.setId(resource.getId());
            treeVo.setPId(resource.getParentId());
            treeVo.setName(resource.getResourceName());
            treeVo.setOpen(Boolean.TRUE);
            listHandle.add(treeVo);
        }
        return listHandle;
    }

    @Override
    public List<TreeVo> findAllOrderBySortNoAscChecked(String resourceIds) {
        List<TreeVo> treeVoIterable = this.findAllOrderBySortNoAsc();
        String[] resourceId = resourceIds.split(",");
        for (String id : resourceId) {
            for (TreeVo treeVo : treeVoIterable) {
                treeVo.setOpen(Boolean.TRUE);
                if (treeVo.getId().equals(id)) {
                    treeVo.setChecked(Boolean.TRUE);
                }
            }
        }
        return treeVoIterable;
    }
}
