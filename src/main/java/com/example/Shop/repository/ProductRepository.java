package com.example.Shop.repository;

import com.example.Shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Khai báo hàm này để Controller gọi được
    @Query("SELECT p FROM Product p WHERE p.deleted = false")
    List<Product> findAllActiveProducts();

    // Hoặc dùng tên này nếu bạn muốn dùng Query Method chuẩn của Spring
    List<Product> findByDeletedFalse();
}