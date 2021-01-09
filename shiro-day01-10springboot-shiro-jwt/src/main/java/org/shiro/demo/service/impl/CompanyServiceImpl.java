package org.shiro.demo.service.impl;

import org.shiro.demo.entity.Company;
import org.shiro.demo.mapper.CompanyMapper;
import org.shiro.demo.service.ICompanyService;
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
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements ICompanyService {

}
