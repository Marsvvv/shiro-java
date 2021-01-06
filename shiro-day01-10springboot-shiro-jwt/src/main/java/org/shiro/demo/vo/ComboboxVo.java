package org.shiro.demo.vo;

import lombok.Data;

/**
 * 复选框
 *
 * @author asus
 */
@Data
public class ComboboxVo {

    /**
     * 传递值
     **/
    private String id;

    /**
     * 显示值
     **/
    private String text;

    /**
     * 是否选择
     **/
    private Boolean selected;
}
