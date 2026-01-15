package com.example.Shop.dto;

import lombok.Data;
import java.util.List;

@Data // <-- Nếu dòng này báo xám/đỏ, nghĩa là chưa có thư viện Lombok
public class OrderRequest {
    private String customerName;
    private String address;
    private String phoneNumber; // <-- Kiểm tra chữ N viết hoa
    private List<CartItem> items;

    @Data
    public static class CartItem {
        private Long productId;
        private Integer quantity;
    }
}