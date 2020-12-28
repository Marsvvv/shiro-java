package org.shiro.demo.properties;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * properties 工具类
 */
public class PropertiesUtils {

    /**
     * 读取properties中的配置信息
     *
     * @return Properties
     */
    /*public static Properties readProperties(String path) {
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
    }*/
    public static Properties readProperties(String path) {
        LinkedProperties properties = new LinkedProperties();
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
}
