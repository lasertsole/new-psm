package com.psm.infrastructure.config;

import com.psm.annotation.spring.impl.AdaptorBeanDefinitionRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(AdaptorBeanDefinitionRegistry.class)
public class AppConfig {
}
