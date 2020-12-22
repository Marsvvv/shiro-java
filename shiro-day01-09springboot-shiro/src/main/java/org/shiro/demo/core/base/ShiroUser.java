package org.shiro.demo.core.base;


import lombok.Data;
import org.shiro.demo.entity.User;

import java.util.List;

/**
 * 将资源列表集成到属性中
 * 方便前后端分离 方便资源列表的使用
 */
@Data
public class ShiroUser extends User {

    /**
     * 资源列表
     */
    private List<String> resourceIds;
}
