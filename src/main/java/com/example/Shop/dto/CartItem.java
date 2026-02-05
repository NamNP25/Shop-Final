package com.example.Shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
    private Long productId; // Số 1
    private String name;      // Số 2
    private Double price;     // Số 3
    private int quantity;     // Số 4
    private String image;     // Số 5
}