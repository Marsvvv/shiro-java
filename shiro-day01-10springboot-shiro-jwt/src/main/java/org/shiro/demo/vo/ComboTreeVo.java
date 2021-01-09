package org.shiro.demo.vo;

import lombok.Data;

import java.util.List;


/**
 * 选择框树显示
 *
 * @author asus
 */
@Data
public class ComboTreeVo {

    /**
     * 传递的Id
     **/
    private String id;

    /**
     * 父Id
     **/
    private String parentId;

    /**
     * 显示的内容
     **/
    private String text;

    /**
     * 是否节点展开
     **/
    private String state;

    /**
     * 是否选择
     **/
    private Boolean checked;

    private List<ComboTreeVo> children;
}
