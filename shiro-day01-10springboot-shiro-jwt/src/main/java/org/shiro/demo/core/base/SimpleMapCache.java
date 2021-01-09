package org.shiro.demo.core.base;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * 自定义 Realm 缓存对象
 */
public class SimpleMapCache implements Cache<Object, Object>, Serializable {

    private final Map<Object, Object> map;
    private final String name;

    public SimpleMapCache(String name, Map<Object, Object> backingMap) {
        if (name == null) {
            throw new IllegalArgumentException("Cache name cannot be null.");
        } else if (backingMap == null) {
            throw new IllegalArgumentException("Backing map cannot be null.");
        } else {
            this.name = name;
            this.map = backingMap;
        }
    }

    @Override
    public Object get(Object key) throws CacheException {
        return this.map.get(key);
    }

    @Override
    public Object put(Object key, Object value) throws CacheException {
        return this.map.put(key, value);
    }

    @Override
    public Object remove(Object key) throws CacheException {
        return this.map.remove(key);
    }

    @Override
    public void clear() throws CacheException {
        this.map.clear();
    }

    @Override
    public int size() {
        return this.map.size();
    }

    @Override
    public Set<Object> keys() {
        Set<Object> keys = this.map.keySet();
        return !keys.isEmpty() ? Collections.unmodifiableSet(keys) : Collections.emptySet();
    }

    @Override
    public Collection<Object> values() {
        Collection<Object> values = this.map.values();
        return (Collection) (!this.map.isEmpty() ? Collections.unmodifiableCollection(values) : Collections.emptySet());
    }

    @Override
    public String toString() {
        return "SimpleMapCache{" +
                "map=" + map +
                ", name='" + name + '\'' +
                '}';
    }
}
