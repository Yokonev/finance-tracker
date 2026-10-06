package com.yokonev.fintrack.service;

import java.util.NoSuchElementException;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.yokonev.fintrack.configuration.SecurityUser;
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
    public UserDetails loadUserByUsername(String email) {
        return userRepo.findByEmail(email)
            .map(SecurityUser::new)
            .orElseThrow(() -> new UsernameNotFoundException("No user with email " + email));
    }

    @Override
    public UserResponse getUserById(Long userId) {
        AppUser user = userRepo.findById(userId)
            .orElseThrow(() -> new NoSuchElementException("No user with id " + userId));
        return toResponse(user);
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        return toResponse(findByEmail(email));
    }

    private AppUser findByEmail(String email) {
        return userRepo.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException(email));
    }
    
    private static UserResponse toResponse(AppUser user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail());
    }

}
