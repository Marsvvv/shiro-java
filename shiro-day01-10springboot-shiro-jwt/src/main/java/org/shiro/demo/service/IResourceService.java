package org.shiro.demo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.shiro.demo.entity.Resource;
import com.baomidou.mybatisplus.extension.service.IService;
import org.shiro.demo.vo.TreeVo;

import java.util.List;

/**
 * <p>
 * 资源表 服务类
 * </p>
 *
 * @author yjw
 * @since 2020-12-22
 */
public interface IResourceService extends IService<Resource> {

    /**
     * 资源多条件查询
     *
     * @param resource resource
     * @param rows     rows
     * @param page     page
     * @return List<Resource>
     */
    IPage<Resource> findResourceList(Resource resource, Integer rows, Integer page);

    /**
     * 修改或修改资源对象
     *
     * @param resource resource
     */
    void saveOrUpdateResource(Resource resource);

    /**
     * 按照标识查询资源
     *
     * @param label label
     * @return String
     */
    String findByLabel(String label);

    /**
     * 按父Id查询树
     *
     * @param parentId parentId
     * @return List<TreeVo>
     */
    List<TreeVo> findResourceTreeVoByParentId(String parentId);

    /**
     * 查找所有树按SortNo降序
     *
     * @return List<TreeVo>
     */
    List<TreeVo> findAllOrderBySortNoAsc();

    /**
     * 查找所有树按SortNo降序,并初始化已选选项
     *
     * @param resourceIds resourceIds
     * @return List<TreeVo>
     */
    List<TreeVo> findAllOrderBySortNoAscChecked(String resourceIds);

}
