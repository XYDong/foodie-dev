package com.joker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @version 1.0.0
 * @ClassName Swagger2.java
 * @Package com.joker.config
 * @Author Joker
 * @Description 接口文档配置
 * @CreateTime 2021年07月26日 11:31:00
 */
@Configuration
@EnableSwagger2
public class Swagger2 {
    // 默认地址： http://localhost:8088/swagger-ui.html
    // 默认地址： http://localhost:8088/doc.html
    // 配置swagger2 核心配置 docket
    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2) // 指定swagger版本为2
                .apiInfo(apiInfo())                     //用于定义api文档汇总信息
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.joker.controller")) // z指定controller包
                .paths(PathSelectors.any())
                .build();

    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("天天吃货 电商平台api")   // 文档标题
                .contact(new Contact("joker",
                        "http://d.xxjoker.top",  // 网站地址
                        "shuiqingshuying@gmail.com")) // 联系人地址
                .description("天天吃货API文档") // 详细信息
                .version("1.0.1") // 版本号
                .termsOfServiceUrl("http://d.xxjoker.top") // 网站地址
                .build();
    }
}
