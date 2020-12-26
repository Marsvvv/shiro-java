package org.shiro.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author Tobu
 */
@SpringBootApplication
@MapperScan("org.shiro.demo.mapper")
public class ShiroDay0109springbootShiroApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ShiroDay0109springbootShiroApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ShiroDay0109springbootShiroApplication.class);
    }
}
