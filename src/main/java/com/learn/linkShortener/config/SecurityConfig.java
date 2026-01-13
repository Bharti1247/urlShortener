package com.learn.linkShortener.config;

import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Public redirect
                .requestMatchers(HttpMethod.GET, "/{shortCode}").permitAll()

                // Only ADMIN can enable/disable
                .requestMatchers(HttpMethod.PATCH, "/{shortCode}/status").hasRole("ADMIN")

                // Other URL APIs require authentication
                .requestMatchers("/api/**").authenticated()

                // Everything else
                .anyRequest().permitAll()
            )
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }
    
    @Bean
    public UserDetailsService userDetailsService() {

        UserDetails admin = User.withUsername("admin")
                .password("{noop}admin123")
                .roles("ADMIN")
                .build();

        UserDetails ops = User.withUsername("ops")
                .password("{noop}ops123")
                .roles("OPS")
                .build();

        return new InMemoryUserDetailsManager(admin, ops);
    }
}

