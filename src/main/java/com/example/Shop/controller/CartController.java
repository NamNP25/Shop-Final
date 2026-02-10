package com.example.Shop.controller;

import com.example.Shop.dto.CartItem;
import com.example.Shop.entity.Product;
import com.example.Shop.repository.ProductRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired private ProductRepository productRepository;

    @GetMapping("/add/{id}")
    public String addToCart(@PathVariable Long id,
                            HttpSession session,
                            HttpServletRequest request,
                            RedirectAttributes redirectAttributes) {

        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }

        Product product = productRepository.findById(id).orElseThrow();

        boolean isExist = false;
        for (CartItem item : cart) {
            if (item.getProductId().equals(id)) {
                item.setQuantity(item.getQuantity() + 1);
                isExist = true;
                break;
            }
        }

        if (!isExist) {
            cart.add(new CartItem(product.getId(), product.getName(), product.getPrice(), 1, product.getImage()));
        }

        session.setAttribute("cart", cart);

        // Thông báo nhỏ hiện lên ở trang hiện tại
        redirectAttributes.addFlashAttribute("cartMsg", "Đã thêm " + product.getName() + " vào giỏ hàng!");

        // Quay lại trang cũ (Referer)
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        double total = 0;
        if (cart != null) {
            total = cart.stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
        }
        model.addAttribute("cart", cart);
        model.addAttribute("total", total);
        return "cart";
    }

    @GetMapping("/remove/{id}")
    public String removeFromCart(@PathVariable Long id, HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart != null) {
            cart.removeIf(item -> item.getProductId().equals(id));
        }
        return "redirect:/cart";
    }
}