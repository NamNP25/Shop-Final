package com.example.Shop.service;

import com.example.Shop.entity.Product;
import com.example.Shop.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public void updateStock(Long productId, int additionalQty) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        // Cộng dồn số lượng mới vào số lượng cũ
        int newStock = product.getStock() + additionalQty;
        if (newStock < 0) throw new RuntimeException("Số lượng kho không thể âm");

        product.setStock(newStock);
        productRepository.save(product);
    }
}
