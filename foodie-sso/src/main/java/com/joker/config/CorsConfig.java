package com.joker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @version 1.0.0
 * @ClassName CorsConfig.java
 * @Package com.joker.config
 * @Author Joker
 * @Description 跨域问题解决
 * @CreateTime 2021年07月26日 12:50:00
 */
@Configuration
public class CorsConfig {
    public CorsConfig() {
    }
    @Bean
    public CorsFilter corsFilter(){
        // 1.添加cors配置信息
        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.addAllowedOrigin("http:localhost:8080");
        corsConfiguration.addAllowedOrigin("*");
        // 设置是否发送cookie信息
        corsConfiguration.setAllowCredentials(true);
        // 设置允许请求的方式
        corsConfiguration.addAllowedMethod("*");
        // 设置允许的header
        corsConfiguration.addAllowedHeader("*");

        // 2.为url添加映射路径
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration("/**",corsConfiguration);
        // 返回重新定义好的corsSource
        return new CorsFilter(configurationSource);
    }
}
