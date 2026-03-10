package com.example.Shop.controller;

import com.example.Shop.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired private OrderService orderService;

    @PostMapping("/checkout")
    public String checkout(@RequestParam String customerName,
                           @RequestParam String address,
                           @RequestParam String phoneNumber,
                           HttpSession session,
                           RedirectAttributes ra) {
        try {
            orderService.placeOrder(customerName, address, phoneNumber, session);
            ra.addFlashAttribute("successMsg", "Đặt hàng thành công!");
            return "redirect:/"; // Về trang chủ báo thành công
        } catch (RuntimeException e) {
            // Bắt đúng cái lỗi "Chỉ còn 5 sản phẩm" và gửi ngược lại trang giỏ hàng
            ra.addFlashAttribute("errorMsg", e.getMessage());
            return "redirect:/cart";
        }
    }
}