package com.psm.infrastructure.DB.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.psm.infrastructure.DB.utils.CustomIdentifierGenerator;
import com.tangzc.autotable.springboot.EnableAutoTable;
import com.tangzc.mpe.condition.DynamicConditionInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@Configuration
@EnableAutoTable
@EnableTransactionManagement // 开启事务支持
public class MyBatisPlusConfig {//mybatis-plus配置类
    @Value("${mybatis-plus.workerId}")
    private Long workerId;

    @Value("${mybatis-plus.datacenterId}")
    private Long datacenterId;

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 添加动态条件，若同时添加了其他的拦截器，继续添加即可
        interceptor.addInnerInterceptor(new DynamicConditionInterceptor());// 配置动态条件插件
        // 如果使用了分页，请放在DynamicConditionInterceptor之后
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.POSTGRE_SQL));//配置mybatis-plus的分页插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());//配置mybatis-plus的乐观锁插件
        return interceptor;
    }

    @Bean
    public IdentifierGenerator identifierGenerator() {
        return new CustomIdentifierGenerator(workerId, datacenterId);
    }
}
