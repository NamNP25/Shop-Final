package com.example.Shop.service;

import com.example.Shop.dto.UserRequestDTO;
import com.example.Shop.dto.UserResponseDTO;
import com.example.Shop.entity.User;
import com.example.Shop.repository.UserRepository;
import org.springframework.stereotype.Service;

/**
 *  Xử lý logic - nghiệp vụ
 *  Nghiệp vụ: Register, Login, Update Profile, Change Password, ...
 *
 *  DI: Dependency Injection
 *
 *  -> UserService phụ thuộc UserRepository để lưu dữ liệu người dùng xuống db
 * */

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDTO register(UserRequestDTO dto) {

        // Bước (1)Map dữ liệu từ UserRequestDTO -> User entity
        User user = mappingRequestToEntity(dto);

        // Bước (2) luu user entity xuống db thông qua repository
        User saved = userRepository.save(user); // Username, email, password, role

        // Bước (3) Map dữ liệu từ User entity -> UserResponseDTO
        UserResponseDTO responseDTO = mappingEntityToResponse(saved); //FE  chỉ nhận:  username + email
        return responseDTO;
    }

    public UserResponseDTO login(User dto) {

        // Bước (1): tìm user theo username
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("Username không tồn tại"));

        // Bước (2): kiểm tra password
        if (!user.getPassword().equals(dto.getPassword())) {
            throw new RuntimeException("Password không đúng");
        }

        // Bước (3): map Entity -> ResponseDTO
        return mappingEntityToResponse(user);
    }

    private User mappingRequestToEntity (UserRequestDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        return user;
    }

    private UserResponseDTO mappingEntityToResponse (User user) {
        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setUsername(user.getUsername());
        responseDTO.setEmail(user.getEmail());
        return responseDTO;
    }
}