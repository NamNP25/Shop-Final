package com.example.Shop.controller;

import com.example.Shop.dto.CartItem;
import com.example.Shop.entity.Product;
import com.example.Shop.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ProductService productService;

    // Helper method để lấy giỏ hàng từ session an toàn
    private List<CartItem> getCartFromSession(HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        List<CartItem> cart = getCartFromSession(session);
        double total = cart.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();

        model.addAttribute("cart", cart);
        model.addAttribute("total", total);
        return "cart";
    }

    @GetMapping("/add/{id}")
    public String addToCart(@PathVariable Long id, HttpSession session) {
        // 1. Lấy giỏ hàng từ session
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }

        // 2. Tìm sản phẩm trong DB
        Product product = productService.getProductById(id);
        if (product == null) {
            return "redirect:/"; // Tránh lỗi null nếu ID sai
        }

        // 3. Kiểm tra trùng lặp
        boolean isExist = false;
        for (CartItem item : cart) {
            if (item.getProductId().equals(id)) {
                item.setQuantity(item.getQuantity() + 1);
                isExist = true;
                break;
            }
        }

        // 4. Nếu chưa có thì thêm mới (PHẢI KHỚP THỨ TỰ Ở BƯỚC 1)
        if (!isExist) {
            cart.add(new CartItem(
                    product.getId(),     // productId
                    product.getName(),   // name
                    product.getPrice(),  // price
                    1,                   // quantity
                    product.getImage()   // image
            ));
        }

        // 5. Lưu lại vào session và chuyển hướng
        session.setAttribute("cart", cart);
        return "redirect:/cart";
    }

    @GetMapping("/remove/{id}")
    public String removeFromCart(@PathVariable Long id, HttpSession session) {
        List<CartItem> cart = getCartFromSession(session);
        cart.removeIf(item -> item.getProductId().equals(id));
        return "redirect:/cart";
    }

    // MỚI: Thêm chức năng cập nhật số lượng (ví dụ nút + hoặc -)
    @GetMapping("/update/{id}/{quantity}")
    public String updateQuantity(@PathVariable Long id, @PathVariable int quantity, HttpSession session) {
        List<CartItem> cart = getCartFromSession(session);
        for (CartItem item : cart) {
            if (item.getProductId().equals(id)) {
                if (quantity > 0) {
                    item.setQuantity(quantity);
                } else {
                    cart.remove(item);
                }
                break;
            }
        }
        return "redirect:/cart";
    }
}