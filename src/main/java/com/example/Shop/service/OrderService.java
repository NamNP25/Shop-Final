package com.example.Shop.service;

import com.example.Shop.dto.CartItem;
import com.example.Shop.entity.*;
import com.example.Shop.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    @Autowired private OrderRepository orderRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private CartService cartService;

    @Transactional(rollbackFor = Exception.class) // Đảm bảo rollback nếu có bất kỳ lỗi nào
    public Order placeOrder(String customerName, String address, String phoneNumber, HttpSession session) {
        Map<Long, CartItem> cart = cartService.getCart(session);

        if (cart == null || cart.isEmpty()) {
            throw new RuntimeException("Giỏ hàng trống, không thể đặt hàng!");
        }

        Order order = new Order();
        order.setCustomerName(customerName);
        order.setAddress(address);
        order.setPhoneNumber(phoneNumber);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");

        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0;

        for (CartItem item : cart.values()) {
            // Sử dụng findById và khóa dòng này lại để tránh xung đột dữ liệu (Locking)
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm ID: " + item.getProductId()));

            int currentStock = (product.getStock() != null) ? product.getStock() : 0;

            if (currentStock < item.getQuantity()) {
                throw new RuntimeException("Sản phẩm [" + product.getName() + "] chỉ còn " + currentStock + " sản phẩm. Vui lòng kiểm tra lại!");
            }

            // Thực hiện trừ kho
            product.setStock(currentStock - item.getQuantity());

            // Ép buộc lưu thay đổi sản phẩm trước khi lưu đơn hàng
            productRepository.saveAndFlush(product);

            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProduct(product);
            oi.setQuantity(item.getQuantity());
            oi.setPrice(item.getPrice());
            orderItems.add(oi);

            total += (item.getPrice() * item.getQuantity());
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(total);

        // Lưu đơn hàng
        Order savedOrder = orderRepository.save(order);

        // Chỉ xóa giỏ hàng khi mọi thứ đã xong xuôi
        session.removeAttribute("cart");

        return savedOrder;
    }
}