package com.psm.infrastructure.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.psm.infrastructure.utils.MybatisPlus.CustomIdentifierGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@Configuration
@EnableTransactionManagement // 开启事务支持
public class MyBatisPlusConfig {//mybatis-plus配置类
    @Value("${mybatis-plus.workerId}")
    private Long workerId;

    @Value("${mybatis-plus.datacenterId}")
    private Long datacenterId;

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));//配置mybatis-plus的分页插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());//配置mybatis-plus的乐观锁插件
        return interceptor;
    }

    @Bean
    public IdentifierGenerator identifierGenerator() {
        return new CustomIdentifierGenerator(workerId, datacenterId);
    }
}
