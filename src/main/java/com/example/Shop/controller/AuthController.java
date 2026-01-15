package com.example.Shop.controller;

import com.example.Shop.dto.UserRequestDTO;
import com.example.Shop.dto.UserResponseDTO;
import com.example.Shop.entity.User;
import com.example.Shop.repository.UserRepository;
import com.example.Shop.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final UserService userService;

    public AuthController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    /**
     * Client -> gửi Request lên
     *
     * BE  -> Validate: dữ liệu hợp lệ (có giá trị không,...)
     *
     * xử lí logic nghiệp vụ ở Service layer: Lỗi nghiệp vụ | Lỗi về dữ liệu (không trùng username, email)
     *
     * trả ra cho Controller -> trả về cho Client
     *
     * */

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequestDTO userRequestDTO) {
        try {
            if (userRequestDTO.getUsername() == null || userRequestDTO.getEmail() == null || userRequestDTO.getPassword() == null) {
                return ResponseEntity.badRequest().body("Lỗi: username, email và password là bắt buộc!");
            }

            // Gọi đến UserService để xử lý logic
            UserResponseDTO register = userService.register(userRequestDTO);

            return ResponseEntity.ok(register);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi hệ thống: " + e.getMessage());
        }
    }

    // --- THÊM HÀM LOGIN NÀY ---
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {

        /**Gọi đến Login() ở Service layer - Chuyên xử lý logic nghiệp vụ*/
        UserResponseDTO user = userService.login(loginRequest);
        return ResponseEntity.ok(user); // Trả về thông tin user nếu đúng pass
    }

    /*
    {
       message: "Sai mật khẩu rồi bạn ơi!",
       data: {
//           username: "john_doe",
//           email: user1@gmail.com"
            null
       }
       responseTime: "2026-01-10T10:10:10Z"
    }

    **/


}