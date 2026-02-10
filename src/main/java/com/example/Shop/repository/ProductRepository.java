package com.example.Shop.repository;

import com.example.Shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Chỉ lấy sản phẩm có deleted = false
    List<Product> findByDeletedFalse();
}