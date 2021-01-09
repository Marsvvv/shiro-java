package org.shiro.demo.properties;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * @author asus
 */
@Getter
@Setter
public class LinkedProperties extends Properties {

    /**
     * 按照顺序插入key，防止过滤器失效
     */
    private List<Object> keyList = new ArrayList<>();

    @Override
    public synchronized Object put(Object key, Object value) {
        keyList.add(key);
        return super.put(key, value);
    }
}
