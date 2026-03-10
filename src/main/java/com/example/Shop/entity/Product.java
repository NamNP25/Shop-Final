package com.example.Shop.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product") // SỬA: Đổi từ "products" thành "product"
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double price;
    private Integer stock = 0;
    private String image;
    private Boolean deleted = false;

    @ManyToMany
    @JoinTable(
            name = "product_categories",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories = new ArrayList<>();

    // --- GETTER ---
    public Long getId() { return id; }
    public String getName() { return name; }
    public Double getPrice() { return price; }
    public Integer getStock() { return stock; }
    public String getImage() { return image; }
    public Boolean getDeleted() { return deleted; }
    public List<Category> getCategories() { return categories; }

    // --- SETTER ---
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPrice(Double price) { this.price = price; }
    public void setStock(Integer stock) { this.stock = stock; }
    public void setImage(String image) { this.image = image; }
    public void setDeleted(Boolean deleted) { this.deleted = deleted; }
    public void setCategories(List<Category> categories) { this.categories = categories; }
}