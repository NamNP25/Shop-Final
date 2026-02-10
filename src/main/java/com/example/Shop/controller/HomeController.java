package com.example.Shop.controller;

import com.example.Shop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/")
    public String trangChu(Model model) {
        // Lấy tất cả sản phẩm để hiển thị ở trang chủ
        model.addAttribute("products", productRepository.findByDeletedFalse());
        return "index"; // Trỏ về file index.html
    }
}