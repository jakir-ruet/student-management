package com.jakirbd.student_management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                // Swagger / OpenAPI
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",

                                // Auth
                                "/api/auth/register",
                                "/api/auth/login",
                                "/api/auth/change-password",
                                "/api/auth/lock/**",
                                "/api/auth/unlock/**",

                                // Student
                                "/api/students/**",
                                "/api/student-addresses/**",
                                "/api/student-guardians/**",

                                // Teacher
                                "/api/teachers/**",
                                "/api/teacher-addresses/**",
                                "/api/teacher-contacts/**",

                                // Subjects
                                "/api/subjects/**",
                                "/api/class-subjects/**",
                                "/api/teacher-subject-assignments/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.disable())
                .httpBasic(httpBasic -> httpBasic.disable());

        return http.build();
    }
}