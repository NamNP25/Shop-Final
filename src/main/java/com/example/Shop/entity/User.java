package com.example.Shop.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users") // Đảm bảo tên bảng trong DB là users
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String email;
    private String fullName;
    private String role; // Ví dụ: ADMIN hoặc USER
}