package com.NextStepEdu.config;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.servlet.ServletContext;

@Configuration
public class MultipartLimitConfig implements ServletContextInitializer {

    @Override
    public void onStartup(ServletContext servletContext) {
        System.out.println("⚙️  Initializing multipart limits via ServletContextInitializer...");

        // Set the multipart max file count via servlet context
        try {
            // This is called during Tomcat initialization
            System.setProperty("org.apache.catalina.fileupload.maxFileCount", "10000");
            System.setProperty("org.apache.tomcat.util.http.fileupload.impl.FileUploadBase.maxFileCount", "10000");
            System.out.println("✅ Multipart limits set via ServletContextInitializer");
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }
}

