package com.yokonev.fintrack.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.yokonev.fintrack.dto.UserResponse;
import com.yokonev.fintrack.entity.AppUser;
import com.yokonev.fintrack.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepo;

    public UserServiceImpl(UserRepository userRepo){
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = findByUsername(username);
        return User
            .withUsername(user.getUsername())
            .password(user.getPasswordHash())
            .authorities(List.of())
            .build();
    }

    @Override
    public UserResponse getUserById(Long userId) {
        AppUser user = userRepo.findById(userId)
            .orElseThrow(() -> new NoSuchElementException("No user with id " + userId));
        return toResponse(user);
    }

    @Override
    public UserResponse getUserByUsername(String username) {
        return toResponse(findByUsername(username));
    }

    private AppUser findByUsername(String username) {
        return userRepo.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    private static UserResponse toResponse(AppUser user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail());
    }

}
