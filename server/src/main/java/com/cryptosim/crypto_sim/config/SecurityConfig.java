package com.cryptosim.crypto_sim.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Disable CSRF (Essential for stateless APIs to work)
            .csrf(csrf -> csrf.disable())

            // 2. Configure URL Permissions
            .authorizeHttpRequests(auth -> auth
                // ALLOW PUBLIC ACCESS to these endpoints:
                .requestMatchers("/api/ping", "/api/info").permitAll() 
                .requestMatchers("/api/auth/**").permitAll()           
                // All other requests require authentication
                .anyRequest().authenticated()
            )

            // 3. Make it Stateless (No HTML Sessions)
            // This stops Spring from creating session cookies and redirects
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

            // Note: We removed .formLogin() so it won't redirect you to an HTML page anymore.

        return http.build();
    }
}