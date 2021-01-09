package org.shiro.demo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 自定义密钥
 *
 * @author Tobu
 */
@Data
@ConfigurationProperties(prefix = "custom.jwt")
public class JwtProperties {

    private String base64EncodedSecretKey;
}
