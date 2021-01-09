package org.shiro.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("tp_company")
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("ID")
    private String id;

    /**
     * 是否有效
     */
    @TableField("ENABLE_FLAG")
    private String enableFlag;

    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 公司地址
     */
    private String address;

    /**
     * 企业编码
     */
    private String companyNo;

    /**
     * 法人
     */
    private String boss;

    /**
     * 注册资金
     */
    private String registeredFund;

    /**
     * 注册时间
     */
    private LocalDateTime registeredTime;

    /**
     * 在保人数
     */
    private Integer insuranceNumber;

    /**
     * 状态 0：正常 1：拉黑
     */
    private String state;


}
