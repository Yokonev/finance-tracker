package com.yokonev.fintrack.service;

import com.yokonev.fintrack.dto.UserResponse;

/**
 *
 * <p> User service interface. Lets a user retrieve their own informations. </p>
 */
public interface UserService {

    UserResponse getUserById(Long userId);
    UserResponse getUserByEmail(String email);

}
