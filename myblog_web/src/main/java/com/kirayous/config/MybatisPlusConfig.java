package com.kirayous.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author KiraYous
 * @version V1.0
 * @Package com.kirayous.config
 * @date 2021/9/12 15:27
 */
@EnableTransactionManagement
@Configuration
@MapperScan({"com.kirayous.api.admin.mapper","com.kirayous.api.blog.mapper"})
public class MybatisPlusConfig {

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        return paginationInterceptor;
    }

}