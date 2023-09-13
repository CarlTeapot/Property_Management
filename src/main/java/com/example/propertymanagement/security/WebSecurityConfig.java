package com.example.propertymanagement.security;


import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final DaoAuthenticationProvider provider;
    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/reports/**")
                .hasAnyAuthority("ADMIN", "SUPPORT", "PROPERTY_ADMIN")
                .requestMatchers("/pictures/**")
                .hasAnyAuthority("ADMIN", "SUPPORT", "PROPERTY_ADMIN", "USER")
                .requestMatchers("/properties/**")
                .hasAnyAuthority("ADMIN", "USER", "SUPPORT", "PROPERTY_ADMIN")
                .requestMatchers("/users/**")
                .hasAnyAuthority("ADMIN", "USER", "SUPPORT", "PROPERTY_ADMIN")
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/free/**")
                .permitAll()
                .and()
                .authorizeHttpRequests()
                .requestMatchers("/authorization/**")
                .permitAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(provider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config )
            throws Exception {
        return config.getAuthenticationManager();
    }
}