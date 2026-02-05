package com.example.Shop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadDir = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;

        // Khi web gọi /images/** sẽ trỏ vào thư mục uploads ngoài đời thực
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + uploadDir);
    }
}