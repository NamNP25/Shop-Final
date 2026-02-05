package com.example.Shop.controller;

import com.example.Shop.entity.Product;
import com.example.Shop.repository.ProductRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    private final ProductRepository productRepository;

    // Đường dẫn thư mục uploads nằm ngay tại gốc project
    private final String UPLOAD_DIR = System.getProperty("user.dir") + File.separator + "uploads" + File.separator;

    public AdminProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public String trangQuanLy(Model model) {
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("newProduct", new Product());
        return "quanly";
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute("newProduct") Product product,
                             @RequestParam("imageFile") MultipartFile imageFile) {
        if (!imageFile.isEmpty()) {
            String fileName = saveFile(imageFile);
            product.setImage(fileName);
        }
        productRepository.save(product);
        return "redirect:/admin/products";
    }

    @PostMapping("/edit")
    public String editProduct(@RequestParam("id") Long id,
                              @RequestParam("name") String name,
                              @RequestParam("price") Double price,
                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        Product p = productRepository.findById(id).orElse(null);
        if (p != null) {
            p.setName(name);
            p.setPrice(price);
            if (imageFile != null && !imageFile.isEmpty()) {
                p.setImage(saveFile(imageFile));
            }
            productRepository.save(p);
        }
        return "redirect:/admin/products";
    }

    private String saveFile(MultipartFile file) {
        try {
            File directory = new File(UPLOAD_DIR);
            if (!directory.exists()) directory.mkdirs(); // Tự tạo thư mục uploads nếu chưa có

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename(); // Tránh trùng tên
            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
        return "redirect:/admin/products";
    }
}