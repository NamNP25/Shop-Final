package com.example.Shop.service;

import com.example.Shop.dto.OrderRequest;
import com.example.Shop.entity.Order;
import com.example.Shop.entity.OrderItem;
import com.example.Shop.entity.Product;
import com.example.Shop.repository.OrderRepository;
import com.example.Shop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Order placeOrder(OrderRequest request) {
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setCustomerName(request.getCustomerName());
        order.setAddress(request.getAddress());
        order.setPhoneNumber(request.getPhoneNumber());
        order.setStatus("PENDING");

        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0;

        for (OrderRequest.CartItem itemDTO : request.getItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm ID: " + itemDTO.getProductId()));

            // KIỂM TRA KHO
            if (product.getStock() < itemDTO.getQuantity()) {
                throw new RuntimeException("Sản phẩm " + product.getName() + " không đủ hàng (Còn: " + product.getStock() + ")");
            }

            // TRỪ KHO
            product.setStock(product.getStock() - itemDTO.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setPrice(product.getPrice());
            orderItem.setOrder(order);
            orderItems.add(orderItem);

            totalAmount += product.getPrice() * itemDTO.getQuantity();
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);
        return orderRepository.save(order);
    }

    // Hàm cập nhật trạng thái đơn hàng cho Admin
    public Order updateStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng ID: " + orderId));
        order.setStatus(status.toUpperCase());
        return orderRepository.save(order);
    }
}