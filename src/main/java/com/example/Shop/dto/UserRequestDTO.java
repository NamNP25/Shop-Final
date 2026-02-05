package com.example.Shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Annotation này sẽ tự tạo getFullName(), getUsername(),...
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {
    private String fullName;
    private String username;
    private String email;
    private String password;
}