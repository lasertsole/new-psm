package com.psm.infrastructure.config;

import com.psm.infrastructure.annotation.spring.impl.AdaptorBeanDefinitionRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Slf4j
@Configuration
@Import(AdaptorBeanDefinitionRegistry.class)
public class AppConfig {
}
