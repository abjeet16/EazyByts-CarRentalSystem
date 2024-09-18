package com.EazyBytes.car_Rentel.configuration;

import com.EazyBytes.car_Rentel.services.jwt.UserService;
import com.EazyBytes.car_Rentel.utils.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor // Automatically generates a constructor for all final fields
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil; // Utility for handling JWT token generation, validation, etc.
    private final UserService userService; // Service to load user details from the database

    /**
     * This method is executed every time a request is made. It checks if the request contains a valid JWT token
     * and sets the authentication in the SecurityContextHolder.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization"); // Get the Authorization header from the HTTP request
        final String jwt;
        final String userEMail;

        // Check if the Authorization header is either missing or doesn't start with "Bearer" (which is the expected format)
        if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, "Bearer")) {
            filterChain.doFilter(request, response); // If no token is found, proceed without authentication
            return; // Exit the filter
        }

        // Extract the JWT token (the part after "Bearer")
        jwt = authHeader.substring(7);

        // Extract the user's email (username) from the token using JWTUtil
        userEMail = jwtUtil.extractUserName(jwt);

        // Check if the username (email) is not empty and if there's no authentication in the SecurityContextHolder
        if (StringUtils.isNotEmpty(userEMail) && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Load user details from the userService based on the extracted email
            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(userEMail);

            // Validate the JWT token (check if it is still valid and not expired)
            if (jwtUtil.isTokenValid(jwt, userDetails)) {
                // If the token is valid, create an authentication token (Spring's way of handling authentication)
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()); // Create authentication token with user details and authorities (roles/permissions)

                // Attach additional details from the request (like IP address, session, etc.)
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Create a new SecurityContext and set the authentication
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(authToken);

                // Set the context into the SecurityContextHolder (which is the holder of authentication for the current request)
                SecurityContextHolder.setContext(context);
            }
        }
        // Continue the filter chain (move to the next filter in the chain)
        filterChain.doFilter(request, response);
    }
}

