package org.shiro.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author yjw
 * @since 2020-12-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sh_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId("ID")
    private String id;

    /**
     * 登录名称
     */
    @TableField("LOGIN_NAME")
    private String loginName;

    /**
     * 真实姓名
     */
    @TableField("REAL_NAME")
    private String realName;

    /**
     * 昵称
     */
    @TableField("NICK_NAME")
    private String nickName;

    /**
     * 密码
     */
    @TableField("PASS_WORD")
    private String passWord;

    /**
     * 加密因子
     */
    @TableField("SALT")
    private String salt;

    /**
     * 性别
     */
    @TableField("SEX")
    private Integer sex;

    /**
     * 邮箱
     */
    @TableField("ZIPCODE")
    private String zipcode;

    /**
     * 地址
     */
    @TableField("ADDRESS")
    private String address;

    /**
     * 固定电话
     */
    @TableField("TEL")
    private String tel;

    /**
     * 电话
     */
    @TableField("MOBIL")
    private String mobil;

    /**
     * 邮箱
     */
    @TableField("EMAIL")
    private String email;

    /**
     * 职务
     */
    @TableField("DUTIES")
    private String duties;

    /**
     * 排序
     */
    @TableField("SORT_NO")
    private Integer sortNo;

    /**
     * 是否有效
     */
    @TableField("ENABLE_FLAG")
    private String enableFlag;


}
