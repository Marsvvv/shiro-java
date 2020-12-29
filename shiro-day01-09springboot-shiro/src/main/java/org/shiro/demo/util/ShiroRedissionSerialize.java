package org.shiro.demo.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * 自定义序列化工具
 *
 * @author Tobu
 */
@Slf4j
public class ShiroRedissionSerialize {

    /**
     * 序列化方法
     *
     * @param object object
     * @return String
     */
    public static String serialize(Object object) {
        //判断对象是否为空
        if (BeanUtil.isEmpty(object)) {
            return null;
        }
        //流的操作
        String encodeBase64 = null;

        try (
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos)
        ) {
            oos.writeObject(object);
            //转换字符串
            encodeBase64 = EncodeUtil.encodeBase64(bos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            log.error("流写入异常：{}", e);
        }
        return encodeBase64;
    }

    /**
     * 反序列化方法
     *
     * @param str str
     * @return Object
     */
    public static Object deserialize(String str) {
        //判断是否为空
        if (StrUtil.isEmpty(str)) {
            return null;
        }
        //流从操作
        Object object = null;
        //转换对象
        try (
                ByteArrayInputStream bis = new ByteArrayInputStream(EncodeUtil.decodeBase64(str));
                ObjectInputStream ois = new ObjectInputStream(bis)
        ) {
            object = ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("流读取异常：{}", e);
        }
        return object;
    }
}
