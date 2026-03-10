package com.example.Shop.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartItem {
    private Long productId;
    private String name;
    private Double price;
    private int quantity;
    private String image;

    // Constructor thủ công để CartService gọi không bị lỗi
    public CartItem(Long productId, String name, Double price, int quantity, String image) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
    }

    // Getter/Setter viết tay để đảm bảo IDE nhận diện 100%
    public Long getProductId() { return productId; }
    public String getName() { return name; }
    public Double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getImage() { return image; }

    public Double getTotalPrice() {
        return (this.price != null) ? this.price * this.quantity : 0.0;
    }
}