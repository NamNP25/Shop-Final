package com.example.Shop.controller;

import com.example.Shop.dto.CartItem;
import com.example.Shop.entity.*;
import com.example.Shop.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
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
                           @RequestParam String customerName,
                           @RequestParam String phoneNumber,
                           @RequestParam String address,
                           RedirectAttributes redirectAttributes) {

        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) return "redirect:/cart";

        User user = userRepository.findByUsername(authentication.getName()).orElse(null);

        Order order = new Order();
        order.setUser(user);
        order.setCustomerName(customerName);
        order.setPhoneNumber(phoneNumber);
        order.setAddress(address);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");

        double total = cart.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
        order.setTotalAmount(total);

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

        orderRepository.save(order);
        session.removeAttribute("cart");

        redirectAttributes.addFlashAttribute("successMsg", "Đặt hàng thành công!");
        return "redirect:/";
    }
}