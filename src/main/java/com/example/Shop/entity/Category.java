package com.example.Shop.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import java.util.List;

@Entity
@Table(name = "categories")
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category")
    @ToString.Exclude // Quan trọng: Ngăn vòng lặp vô tận khi in log hoặc render
    private List<Product> products;
}