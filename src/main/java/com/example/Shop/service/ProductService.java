package com.example.Shop.service;

import com.example.Shop.entity.Product;
import com.example.Shop.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public void softDelete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        product.setDeleted(true); // 👈 KHÔNG DELETE
        productRepository.save(product);
    }
}
