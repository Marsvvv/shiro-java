package org.shiro.demo.service.impl;

import org.shiro.demo.entity.Resource;
import org.shiro.demo.mapper.ResourceMapper;
import org.shiro.demo.service.IResourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
