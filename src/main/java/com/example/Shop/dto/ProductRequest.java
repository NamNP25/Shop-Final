package com.example.Shop.dto;

import org.springframework.web.multipart.MultipartFile;
import lombok.Data;

@Data
public class ProductRequest {
    private String name;
    private Double price;
    private Integer stock;
    private String description;
    private String imageUrl; // Nhận link ảnh dán vào
    private MultipartFile imageFile; // Nhận file upload
    private Long categoryId; // Quan trọng: Fix lỗi image_721d8c
}