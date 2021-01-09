package org.shiro.demo.core.base;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.shiro.authc.UsernamePasswordToken;

import java.io.Serializable;

/**
 * 自定义token
 */
@Data
public class SimpleToken extends UsernamePasswordToken implements Serializable {

    private String tokenType;

    private String quickPassword;

    /**
     * Constructor for SimpleToken
     *
     * @param tokenType tokenType
     */
    public SimpleToken(String tokenType, String username, String password) {
        super(username, password);
        this.tokenType = tokenType;
    }
}
