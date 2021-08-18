package com.joker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
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
// 扫描所有包以及相关组件包
@ComponentScan(basePackages = {"com.joker","org.n3r.idworker"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }

}
