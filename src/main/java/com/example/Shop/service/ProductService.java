package com.example.Shop.service;

import com.example.Shop.dto.ProductRequest;
import com.example.Shop.entity.Category;
import com.example.Shop.entity.Product;
import com.example.Shop.repository.CategoryRepository;
import com.example.Shop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với ID: " + id));
    }

    // 1. Hàm lưu dùng cho API hoặc Logic phức tạp (Dùng DTO)
    public Product saveProduct(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setDescription(request.getDescription());
        product.setImage(request.getImageUrl());

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại với ID: " + request.getCategoryId()));
            product.setCategory(category);
        }
        return productRepository.save(product);
    }

    // 2. Hàm lưu bổ sung để khớp với gọi lệnh từ Controller (Sửa lỗi Cannot resolve method 'save')
    // Chúng ta đặt tên là save() để Controller gọi productService.save(product) hoạt động ngay
    public void save(Product product) {
        productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> searchByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Product> getByCategoryId(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }
}