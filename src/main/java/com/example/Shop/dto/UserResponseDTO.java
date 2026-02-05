package com.example.Shop.dto;

import lombok.Data;

@Data // Nếu dùng Lombok, nó sẽ tự tạo getRole() cho bạn
public class UserResponseDTO {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String role; // THÊM DÒNG NÀY

    // Nếu KHÔNG dùng Lombok, bạn phải viết thủ công:
    /*
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    */
}