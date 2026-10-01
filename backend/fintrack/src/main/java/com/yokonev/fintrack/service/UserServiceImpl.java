package com.yokonev.fintrack.service;

import org.springframework.stereotype.Service;

import com.yokonev.fintrack.dto.UserResponse;

@Service 
public class UserServiceImpl implements UserService {

    @Override
    public UserResponse getUserById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserById'");
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserByEmail'");
    }

    @Override
    public UserResponse createNewUser(String username, String email, String password) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createNewUser'");
    }

    @Override
    public UserResponse updateUserUsername(Long id, String newUsername) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUserUsername'");
    }

    @Override
    public UserResponse updateUserEmail(Long id, String newEmail) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUserEmail'");
    }

    @Override
    public UserResponse updateUserPassword(Long id, String newPassword) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUserPassword'");
    }

    @Override
    public void deleteUser(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteUser'");
    }
    
}
