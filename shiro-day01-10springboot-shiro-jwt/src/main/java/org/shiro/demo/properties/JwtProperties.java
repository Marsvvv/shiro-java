package org.shiro.demo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Jwt配置类 Json Web Token
 *
 * @author asus
 */
@Data
@ConfigurationProperties(prefix = "custom.jwt")
public class JwtProperties {

    /**
     * 签名密码
     */
    private String base64EncodedSecretKey;
}
