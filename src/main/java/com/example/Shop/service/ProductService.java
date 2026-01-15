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

    public Product saveProduct(ProductRequest request) {
        if (request.getCategoryId() == null) {
            throw new RuntimeException("Lỗi: categoryId không được để trống!");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại với ID: " + request.getCategoryId()));

        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategory(category);

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // Các hàm tìm kiếm và lọc (Chỉ giữ lại 1 bản duy nhất)
    public List<Product> searchByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Product> getByCategoryId(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

} // <--- Dấu này cực kỳ quan trọng để kết thúc Class