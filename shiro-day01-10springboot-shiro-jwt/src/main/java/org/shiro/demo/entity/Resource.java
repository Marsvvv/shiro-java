package org.shiro.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 资源表
 * </p>
 *
 * @author yjw
 * @since 2020-12-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sh_resource")
public class Resource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("ID")
    private String id;

    /**
     * 是否有效
     */
    @TableField("ENABLE_FLAG")
    private String enableFlag;

    /**
     * 父Id
     */
    @TableField("PARENT_ID")
    private String parentId;

    /**
     * 资源名称
     */
    @TableField("RESOURCE_NAME")
    private String resourceName;

    /**
     * 资源路径
     */
    @TableField("REQUEST_PATH")
    private String requestPath;

    /**
     * 资源标识
     */
    @TableField("LABEL")
    private String label;

    /**
     * 图标
     */
    @TableField("ICON")
    private String icon;

    /**
     * 是否叶子节点
     */
    @TableField("IS_LEAF")
    private String isLeaf;

    /**
     * 资源类型
     */
    @TableField("RESOURCE_TYPE")
    private String resourceType;

    /**
     * 排序
     */
    @TableField("SORT_NO")
    private Integer sortNo;

    /**
     * 描述
     */
    @TableField("DESCRIPTION")
    private String description;

    /**
     * 系统归属
     */
    @TableField("SYSTEM_CODE")
    private String systemCode;

    /**
     * 是否根节点
     */
    @TableField("IS_SYSTEM_ROOT")
    private String isSystemRoot;

    /**
     * 服务接口全限定路径
     */
    @TableField("SERVICE_NAME")
    private String serviceName;

    /**
     * 方法名
     */
    @TableField("METHOD_NAME")
    private String methodName;

    /**
     * 传入参数全限定类型
     */
    @TableField("METHOD_PARAM")
    private String methodParam;

    /**
     * DUBBO版本号
     */
    @TableField("DUBBO_VERSION")
    private String dubboVersion;

    /**
     * 接口轮训算法
     */
    @TableField("LOADBALANCE")
    private String loadbalance;

    /**
     * 接口超时时间
     */
    @TableField("TIMEOUT")
    private Integer timeout;

    /**
     * 重试次数
     */
    @TableField("RETRIES")
    private Integer retries;


}
