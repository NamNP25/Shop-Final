package com.example.Shop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Trỏ trực tiếp vào thư mục static/images trong source code
        String path = System.getProperty("user.dir") + File.separator + "src" + File.separator +
                "main" + File.separator + "resources" + File.separator + "static" + File.separator + "images" + File.separator;

        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + path)
                .setCachePeriod(0); // Ép trình duyệt không lưu cache để thấy ảnh mới ngay
    }
}