package org.shiro.demo.properties;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * properties 工具类
 */
public class PropertiesUtils {

    /**
     * 读取properties中的配置信息
     *
     * @return Properties
     */
    public static Properties readProperties(String path) {
        Properties properties = new Properties();
        try (
                InputStream inputStream = PropertiesUtils.class.getResourceAsStream(path);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)
        ) {
            properties.load(inputStreamReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    /**
     * 将properties配置信息转化为map形式
     *
     * @param properties properties
     * @return Map
     */
    public static Map<String, String> Properties2Map(Properties properties) {
        Map<String, String> map = new HashMap<>(properties.entrySet().size());
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            map.put(entry.getKey().toString(), entry.getValue().toString());
        }
        return map;
    }
}
