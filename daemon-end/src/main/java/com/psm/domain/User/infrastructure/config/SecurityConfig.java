package com.psm.domain.User.infrastructure.config;

import com.psm.domain.User.infrastructure.filter.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
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
    };

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

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                auth -> auth.requestMatchers("/users/login", "/users/register").anonymous()
                .anyRequest().authenticated());

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.csrf(csrf -> csrf.disable());

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
    };
}
