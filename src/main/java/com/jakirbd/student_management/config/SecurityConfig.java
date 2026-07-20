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
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",

                                "/api/auth/**",

                                "/api/students/**",
                                "/api/student-addresses/**",
                                "/api/student-guardians/**",

                                "/api/teachers/**",
                                "/api/teacher-addresses/**",
                                "/api/teacher-contacts/**",

                                "/api/academic-years/**",
                                "/api/academic-year-classes/**",
                                "/api/sections/**",
                                "/api/section-shifts/**",

                                "/api/subjects/**",
                                "/api/class-subjects/**",
                                "/api/teacher-subject-assignments/**",

                                "/api/attendance-sessions/**",
                                "/api/student-attendance/**",

                                // Examination Management
                                "/api/exams/**",
                                "/api/exam-subjects/**",
                                "/api/grade-scales/**",
                                "/api/student-exam-marks/**"
                        )
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                );

        return http.build();
    }
}