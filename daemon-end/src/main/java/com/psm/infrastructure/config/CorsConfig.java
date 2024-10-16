package com.psm.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


public class CorsConfig implements WebMvcConfigurer {
    // 协议
    @Value("${server.protocol}")
    private String protocol;

    // 前端地址
    @Value("${server.front-end-url.socket}")
    private String frontEndBaseUrl;
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")// 允许所有路径
                .allowedOrigins(protocol+"://"+frontEndBaseUrl)// 允许前端的域名
                .allowedOriginPatterns("*")//TODO 上线时要修改 允许前端的域名
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")// 允许的方法
                .allowedHeaders("*") // 允许所有头部
                .allowCredentials(true)// 是否允许发送凭证（如Cookie）
                .exposedHeaders("*")// 暴露所有头部（一定要设置，不然只有浏览器看得到但js拿不到）
                .maxAge(3600);// 预检请求的有效期
    }
}
