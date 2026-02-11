package com.example.Shop.controller;

import com.example.Shop.entity.Product;
import com.example.Shop.repository.ProductRepository;
import com.example.Shop.repository.CategoryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.nio.file.*;
import java.util.Arrays;

@Tag(name = "Sản phẩm Admin", description = "Quản lý kho hàng cho dự án Shop")
@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final String UPLOAD_DIR = System.getProperty("user.dir") + "/src/main/resources/static/images/";

    public AdminProductController(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Operation(summary = "Xem danh sách sản phẩm", description = "Hiển thị trang HTML quản lý kho hàng")
    @GetMapping
    public String trangQuanLy(Model model) {
        model.addAttribute("products", productRepository.findByDeletedFalse());
        model.addAttribute("categories", categoryRepository.findAll());
        return "quanly";
    }

    @Operation(summary = "Lấy dữ liệu 1 sản phẩm", description = "Trả về JSON để hiển thị lên Modal Sửa")
    @GetMapping("/get/{id}")
    @ResponseBody
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Thêm mới sản phẩm", description = "Thêm mới sản phẩm  , chỉ admin mới có quyền")
    @PostMapping("/add")
    public String addProduct(@ModelAttribute Product product,
                             @RequestParam("imageFile") MultipartFile imageFile,
                             @RequestParam(value = "categoryIds", required = false) Long[] categoryIds,
                             RedirectAttributes ra) {
        processSave(product, imageFile, categoryIds, ra, "Thêm thành công!");
        return "redirect:/admin/products";
    }

    @Operation(summary = "Cập nhật sản phẩm", description = "Cập nhật thông tin sản phẩm đã có ,chỉ admin mới có quyền")
    @PostMapping("/edit")
    public String editProduct(@ModelAttribute Product product,
                              @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                              @RequestParam(value = "categoryIds", required = false) Long[] categoryIds,
                              RedirectAttributes ra) {
        processSave(product, imageFile, categoryIds, ra, "Cập nhật thành công!");
        return "redirect:/admin/products";
    }

    @Operation(summary = "Xóa sản phẩm (Soft Delete)", description = "Xóa sản phẩm khỏi hệ thống")
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes ra) {
        productRepository.findById(id).ifPresent(p -> {
            p.setDeleted(true);
            productRepository.save(p);
        });
        ra.addFlashAttribute("successMsg", "Đã xóa sản phẩm!");
        return "redirect:/admin/products";
    }

    private void processSave(Product product, MultipartFile imageFile, Long[] categoryIds, RedirectAttributes ra, String msg) {
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                File dir = new File(UPLOAD_DIR);
                if (!dir.exists()) dir.mkdirs();
                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                Files.copy(imageFile.getInputStream(), Paths.get(UPLOAD_DIR + fileName), StandardCopyOption.REPLACE_EXISTING);
                product.setImage(fileName);
            } else if (product.getId() != null) {
                productRepository.findById(product.getId()).ifPresent(old -> product.setImage(old.getImage()));
            }
            if (categoryIds != null) {
                product.setCategories(categoryRepository.findAllById(Arrays.asList(categoryIds)));
            }
            product.setDeleted(false);
            productRepository.save(product);
            ra.addFlashAttribute("successMsg", msg);
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Lỗi: " + e.getMessage());
        }
    }
}