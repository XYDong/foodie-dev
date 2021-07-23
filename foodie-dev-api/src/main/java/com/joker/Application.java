package com.joker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @version 1.0.0
 * @ClassName Application.java
 * @Package com.joker
 * @Author Joker
 * @Description 启动类
 * @CreateTime 2021年07月23日 13:54:00
 */
@SpringBootApplication
@MapperScan(basePackages = "com.joker.mapper")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }

}
