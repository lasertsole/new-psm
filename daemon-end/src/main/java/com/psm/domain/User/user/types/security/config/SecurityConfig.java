package com.psm.domain.User.user.types.security.config;

import com.psm.domain.User.user.types.security.filter.JwtAuthenticationTokenFilter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Slf4j
@Setter
@Configuration
@EnableWebSecurity
@ConfigurationProperties(prefix = "spring.security.request-matcher")
public class SecurityConfig {

    private List<String> anonymous;

    private List<String> permitAll;

    @Autowired
    UserDetailsService userDetailsService;

    @Bean
    public org.springframework.security.crypto.password.PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        //关联userDetailsService
        authenticationProvider.setUserDetailsService(userDetailsService);

        //设置密码管理器
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return new ProviderManager(authenticationProvider);
    }

    //JWT认证处理器
    @Autowired
    JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    //拒绝访问处理器
    @Autowired
    AccessDeniedHandler accessDeniedHandler;

    //第三方登录处理
    @Autowired
    private OAuth2UserService OAuth2ThirdAccountServiceDetail;

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Value("${server.protocol}")
    private String protocol;

    // 前端地址
    @Value("${server.front-end-url.socket}")
    private String frontEndBaseUrl;

    /**
     * 跨域配置
     *
     * @return CorsConfigurationSource
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(protocol + "://" + frontEndBaseUrl)); // 允许的源
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")); // 允许的方法
        configuration.setAllowedHeaders(List.of("*")); // 允许的头部
        configuration.setAllowCredentials(true); // 是否允许发送 Cookie
        configuration.setExposedHeaders(List.of("*"));// 暴露所有头部（一定要设置，不然只有浏览器看得到但js拿不到）
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 应用到所有路径
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 配置请求权限
        http.authorizeHttpRequests(
                auth -> auth.requestMatchers(anonymous.toArray(String[]::new)).anonymous()
                        .requestMatchers(permitAll.toArray(String[]::new)).permitAll()
                        .anyRequest().authenticated());

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        //配置第三方登录
        http.oauth2Login(oauth -> oauth
                .loginPage("/users/login")
                .userInfoEndpoint(userInfo -> userInfo.userService(OAuth2ThirdAccountServiceDetail))
                .successHandler(authenticationSuccessHandler)
        );

        //添加过滤器
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        //配置异常处理器
        http.exceptionHandling((exceptions) -> exceptions
                .accessDeniedHandler(accessDeniedHandler)
        );

        return http.build();
    }
}
