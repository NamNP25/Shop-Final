package com.example.Shop.dto;

public class ProductRequest {
    private String name;
    private Double price;
    private Integer stock;
    private Long categoryId; // Tên biến phải là categoryId

    // Getter và Setter thủ công (Chuột phải -> Generate -> Getter and Setter)
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
}