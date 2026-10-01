package com.yokonev.fintrack.service;

import org.springframework.stereotype.Service;

import com.yokonev.fintrack.dto.UserResponse;

@Service 
public interface UserService {
    UserResponse getUserById(Long id);
    UserResponse getUserByEmail(String email);

    UserResponse createNewUser(String username, String email, String password);

    UserResponse updateUserUsername(Long id, String newUsername);
    UserResponse updateUserEmail(Long id, String newEmail);
    UserResponse updateUserPassword(Long id, String newPassword);

    void deleteUser(Long id);
}
