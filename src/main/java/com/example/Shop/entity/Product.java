package com.example.Shop.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double price;
    private String image;

    @Column(nullable = false)
    private Integer stock = 0;

    @Column(nullable = false)
    private boolean deleted = false;

    // Sử dụng EAGER để load danh mục ngay lập tức, tránh lỗi 500
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories = new ArrayList<>();
}