package com.example.librarymanagementsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static com.example.librarymanagementsystem.common.Constants.*;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.withUsername(USER_USERNAME)
                .password(passwordEncoder.encode(USER_PASSWORD))
                .roles(USER_ROLE)
                .build();

        UserDetails admin = User.withUsername(ADMIN_USERNAME)
                .password(passwordEncoder.encode(ADMIN_PASSWORD))
                .roles(USER_ROLE, ADMIN_ROLE)
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(request -> request
                        .requestMatchers(HttpMethod.GET, "/api/books").hasRole(USER_ROLE)
                        .requestMatchers(HttpMethod.GET, "/api/books/{id}").hasRole(USER_ROLE)
                        .requestMatchers(HttpMethod.POST, "/api/books").hasRole(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.PUT, "/api/books/{id}").hasRole(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.DELETE, "/api/books/{id}").hasRole(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.GET, "/api/patrons").hasRole(ADMIN_ROLE)
                        .requestMatchers(HttpMethod.GET, "/api/patrons/{id}").hasRole(USER_ROLE)
                        .requestMatchers(HttpMethod.POST, "/api/patrons").hasRole(USER_ROLE)
                        .requestMatchers(HttpMethod.PUT, "/api/patrons").hasRole(USER_ROLE)
                        .requestMatchers(HttpMethod.DELETE, "/api/patrons").hasRole(USER_ROLE)
                        .requestMatchers(HttpMethod.POST, "/api/borrow/{bookId}/patron/{patronId}").hasRole(USER_ROLE)
                        .requestMatchers(HttpMethod.PUT, "/api/return/{bookId}/patron/{patronId}").hasRole(USER_ROLE)
                        .anyRequest().authenticated())
                .headers(headersConf -> headersConf.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .httpBasic(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
