package com.kirayous.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Value("${enable.swagger2}")
    boolean enableSwagger2;
    @Bean
    public Docket swagger(){
        return   new Docket(DocumentationType.SWAGGER_2)
                .enable(enableSwagger2)
                .apiInfo(apiInfo())
                .select()
                .paths(PathSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("com.kirayous.admin.controller"))
                .build();
    }
    public ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("我的博客文档")
                .version("v1.0")
                .description("springboot+vue的博客系统")
                .build();
    }
}