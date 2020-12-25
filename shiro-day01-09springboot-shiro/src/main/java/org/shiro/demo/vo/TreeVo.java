package org.shiro.demo.vo;

import lombok.Data;

/**
 * 资源树显示类
 *
 * @author asus
 */
@Data
public class TreeVo {

    /**
     * 本节点Id
     **/
    private String id;

    /**
     * 父节点Id
     **/
    private String pId;

    /**
     * 节点名称
     **/
    private String name;

    /**
     * 节点url
     **/
    private String file;

    /**
     * 节点是否被选中
     **/
    private Boolean checked;

    private Boolean isParent = Boolean.TRUE;

    private Boolean open = Boolean.FALSE;

    public TreeVo(String id, String pId, String name) {
        this.id = id;
        this.pId = pId;
        this.name = name;
    }

    public TreeVo() {
    }
}
