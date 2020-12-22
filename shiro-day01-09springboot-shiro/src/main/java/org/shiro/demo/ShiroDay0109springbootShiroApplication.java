package org.shiro.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.shiro.demo.mapper")
public class ShiroDay0109springbootShiroApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShiroDay0109springbootShiroApplication.class, args);
    }

}
