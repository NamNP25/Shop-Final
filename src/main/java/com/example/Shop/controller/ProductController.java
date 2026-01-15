package com.example.Shop.controller;

import com.example.Shop.dto.ProductRequest;
import com.example.Shop.entity.Product;
import com.example.Shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*") // Hỗ trợ kết nối với các ứng dụng Frontend khác
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAll() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.saveProduct(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Đã xóa sản phẩm thành công!");
    }
    // Tìm kiếm: /api/products/search?name=iphone
    @GetMapping("/search")
    public List<Product> search(@RequestParam String name) {
        return productService.searchByName(name);
    }

    // Lọc theo danh mục: /api/products/category/1
    @GetMapping("/category/{categoryId}")
    public List<Product> getByCategory(@PathVariable Long categoryId) {
        return productService.getByCategoryId(categoryId);
    }
}