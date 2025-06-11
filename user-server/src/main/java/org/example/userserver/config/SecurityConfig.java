package org.example.userserver.config;

import org.example.userserver.entity.User;
import org.example.userserver.mapper.UserMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // 根据需求决定是否禁用
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/addUser", "/addAdmin", "/userdelete/**", "/userdelete/all")
                        .hasRole("ADMIN") // 仅管理员可访问
                        .anyRequest().permitAll() // 其他请求开放
                )
                .formLogin(form -> form
                        .loginPage("/login") // 自定义登录页
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserMapper userMapper) {
        return username -> {
            User user = userMapper.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("用户不存在");
            }

            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRole() ? "ADMIN" : "USER")
                    .disabled(!user.getEnabled())
                    .build();
        };
    }
}