package org.shiro.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author yjw
 * @since 2020-12-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sh_filter_chain")
public class FilterChain implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 描述
     */
    private String urlName;

    /**
     * 路径
     */
    private String url;

    /**
     * 拦截器名称
     */
    private String filterName;

    /**
     * 排序
     */
    private Integer sortNo;

    /**
     * 是否有效
     */
    private String enableFlag;

    private String permissions;

    private String roles;


}
