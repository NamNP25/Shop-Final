package com.example.Shop.repository;

import com.example.Shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Tìm kiếm theo tên (chứa từ khóa, không phân biệt hoa thường)
    List<Product> findByNameContainingIgnoreCase(String name);

    // Lọc theo ID danh mục
    List<Product> findByCategoryId(Long categoryId);
}