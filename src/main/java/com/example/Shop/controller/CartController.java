package com.example.Shop.controller;

import com.example.Shop.dto.CartItem;
import com.example.Shop.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // 1. Xem giỏ hàng
    @GetMapping
    public String viewCart(Model model, HttpSession session) {
        Map<Long, CartItem> cart = cartService.getCart(session);
        model.addAttribute("cartItems", cart.values()); // Chuyển Map thành List để Thymeleaf dễ duyệt
        model.addAttribute("totalAmount", cart.values().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity()).sum());
        return "cart"; // Trả về file cart.html
    }

    // 2. Thêm sản phẩm vào giỏ
    @GetMapping("/add/{id}")
    public String addToCart(@PathVariable Long id, HttpSession session, RedirectAttributes ra) {
        try {
            cartService.addToCart(id, session);
            ra.addFlashAttribute("successMsg", "Đã thêm vào giỏ hàng!");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMsg", "Không thể thêm sản phẩm!");
        }
        return "redirect:/"; // Quay lại trang chủ hoặc trang sản phẩm
    }

    // 3. Xóa sản phẩm khỏi giỏ
    @GetMapping("/remove/{id}")
    public String removeFromCart(@PathVariable Long id, HttpSession session) {
        cartService.removeFromCart(id, session);
        return "redirect:/cart";
    }

    // 4. Cập nhật số lượng qua AJAX (Giống đoạn code bạn gửi nhưng chuẩn hóa Map)
    @PostMapping("/update-quantity") // Sửa từ /update thành /update-quantity
    @ResponseBody
    public ResponseEntity<?> updateQuantity(@RequestParam Long id, @RequestParam Integer qty, HttpSession session) {
        try {
            CartItem updatedItem = cartService.updateQuantity(id, qty, session);

            Map<String, Object> response = new HashMap<>();
            // Nếu qty < 1, item bị xóa khỏi Map, trả về removed: true để JS xóa dòng đó trên giao diện
            if (updatedItem == null) {
                response.put("removed", true);
            } else {
                response.put("newQty", updatedItem.getQuantity());
                response.put("itemTotal", updatedItem.getTotalPrice());
            }

            // Tính lại tổng cộng cả giỏ hàng
            double cartTotal = cartService.getCart(session).values().stream()
                    .mapToDouble(CartItem::getTotalPrice).sum();
            response.put("cartTotal", cartTotal);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi cập nhật: " + e.getMessage());
        }
    }
}