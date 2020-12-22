package org.shiro.demo.service.impl;

import org.shiro.demo.entity.FilterChain;
import org.shiro.demo.mapper.FilterChainMapper;
import org.shiro.demo.service.IFilterChainService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yjw
 * @since 2020-12-22
 */
@Service
public class FilterChainServiceImpl extends ServiceImpl<FilterChainMapper, FilterChain> implements IFilterChainService {

}
