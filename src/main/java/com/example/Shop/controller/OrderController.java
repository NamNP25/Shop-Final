package com.example.Shop.controller;

import com.example.Shop.dto.CartItem;
import com.example.Shop.entity.*;
import com.example.Shop.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired private OrderRepository orderRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ProductRepository productRepository;

    @PostMapping("/checkout")
    public String checkout(HttpSession session,
                           Authentication authentication,
                           @ModelAttribute Order order) { // Dùng @ModelAttribute thay cho @RequestParam

        // 1. Lấy giỏ hàng từ session
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            return "redirect:/cart?error=empty_cart";
        }

        // 2. Lấy User đang đăng nhập và gắn vào order
        User user = userRepository.findByUsername(authentication.getName()).orElse(null);
        order.setUser(user);

        // 3. Tính tổng tiền
        double total = cart.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
        order.setTotalAmount(total);

        // 4. Chuyển CartItem sang OrderItem
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem ci : cart) {
            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProduct(productRepository.findById(ci.getProductId()).orElse(null));
            oi.setQuantity(ci.getQuantity());
            oi.setPrice(ci.getPrice());
            orderItems.add(oi);
        }
        order.setOrderItems(orderItems);

        // 5. Lưu vào Database
        orderRepository.save(order);

        // 6. Xóa giỏ hàng
        session.removeAttribute("cart");

        return "redirect:/?success=order_ok";
    }
}