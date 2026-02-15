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

        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            String contentType = request.getContentType();

            // If it's a multipart request and failed, provide better error
            if (contentType != null && contentType.contains("multipart/form-data")) {
                String errorMessage = e.getMessage() != null ? e.getMessage() : "Multipart parsing failed";

                // Log for debugging
                System.err.println("=== MULTIPART ERROR ===");
                System.err.println("Content-Type: " + contentType);
                System.err.println("Method: " + request.getMethod());
                System.err.println("Path: " + request.getRequestURI());
                System.err.println("Error: " + errorMessage);
                e.printStackTrace();
                System.err.println("======================");

                // Re-throw so exception handler can catch it
                throw e;
            }

            throw e;
        }
    }
}

