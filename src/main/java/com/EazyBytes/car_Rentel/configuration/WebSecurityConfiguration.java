package com.EazyBytes.car_Rentel.configuration;

import com.EazyBytes.car_Rentel.enums.UserRole;
import com.EazyBytes.car_Rentel.services.jwt.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // Marks this class as a configuration class for Spring
@EnableWebSecurity // Enables Spring Security for the application
@EnableMethodSecurity // Enables method-level security annotations like @PreAuthorize
@RequiredArgsConstructor // Automatically generates a constructor for final fields (jwtAuthenticationFilter, userService)
public class WebSecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter; // JWT filter to intercept requests and validate JWT tokens
    private final UserService userService; // Custom user service for loading user details

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // Configures security settings for HTTP requests
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable) // Disables CSRF (Cross-Site Request Forgery) protection for simplicity
                .authorizeHttpRequests(request ->
                        request
                                .requestMatchers("/api/auth/**").permitAll() // Allows public access to authentication endpoints (e.g., login, register)
                                //.requestMatchers("/api/admin/**").hasAnyAuthority(UserRole.ADMIN.name()) // Only users with ADMIN role can access /api/admin/**
                                .requestMatchers("/api/user/**").hasAnyAuthority(UserRole.CUSTOMER.name()) // Only users with CUSTOMER role can access /api/user/**
                                .anyRequest().permitAll()//authenticated() // All other requests must be authenticated (i.e., user must be logged in)
                )
                .sessionManagement(manager ->
                        manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Configures stateless sessions since JWT is used for authentication
                )
                .authenticationProvider(authenticationProvider()) // Specifies the custom authentication provider
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Adds the JWT filter before the default authentication filter
        return httpSecurity.build(); // Builds and returns the security filter chain
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Defines the password encoder to be used for hashing passwords
        return new BCryptPasswordEncoder(); // Uses BCrypt hashing algorithm for encoding passwords
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        // Configures the authentication provider, which is responsible for user authentication
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService.userDetailsService()); // Sets the custom UserDetailsService
        authenticationProvider.setPasswordEncoder(passwordEncoder()); // Sets the password encoder for validating passwords
        return authenticationProvider; // Returns the configured authentication provider
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class).build();
    }

}

