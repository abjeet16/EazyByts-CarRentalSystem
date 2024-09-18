package com.EazyBytes.car_Rentel.configuration;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // This ensures the filter runs before other filters
public class SimpleCorseFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // This method is called when the filter is first created. Here we call the parent class init method.
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        // Cast the ServletRequest and ServletResponse into HTTP-specific objects
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        // Create a map to hold origin-related information (though unused here)
        Map<String,String> map = new HashMap<>();

        // Get the 'Origin' header from the request, which indicates where the request is coming from
        String originHeader = request.getHeader("origin");

        // Allow the request to be accessed from the origin in the header
        response.setHeader("Access-Control-Allow-Origin", originHeader);

        // Define which HTTP methods are allowed for CORS requests (POST, GET, PUT, OPTIONS, DELETE)
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,PUT,OPTIONS,DELETE");

        // Indicate how long the CORS preflight response can be cached (3600 seconds = 1 hour)
        response.setHeader("Access-Control-Max-Age", "3600");

        // Allow all headers to be sent in the request
        response.setHeader("Access-Control-Allow-Headers", "*");

        // If the request method is 'OPTIONS' (preflight request), set the response status to OK (200)
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK); // Return OK for preflight requests
        } else {
            // For other request methods (e.g., GET, POST), pass the request down the filter chain
            filterChain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        // Called when the filter is being taken out of service, here we call the parent destroy method.
        Filter.super.destroy();
    }
}

