package com.casair.cba_work.config

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor
import org.mybatis.spring.annotation.MapperScan
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@MapperScan("com.casair.cba_work.repository")
class MyBatisPlusConfig {

    @Bean
    fun paginationInterceptor() = PaginationInterceptor()
}