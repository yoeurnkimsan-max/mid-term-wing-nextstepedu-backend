package com.NextStepEdu.security;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MultipartExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String contentType = request.getContentType();
        String method = request.getMethod();
        String path = request.getRequestURI();

        // Log multipart requests for debugging
        if (contentType != null && contentType.contains("multipart")) {
            System.out.println("[MULTIPART REQUEST] Method: " + method + " | Path: " + path);
            System.out.println("[CONTENT-TYPE] " + contentType);
        }

        // Continue the filter chain normally
        filterChain.doFilter(request, response);
    }
}

