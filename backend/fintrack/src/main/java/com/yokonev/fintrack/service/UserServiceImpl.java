package com.yokonev.fintrack.service;

import java.util.List;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.yokonev.fintrack.entity.AppUser;
import com.yokonev.fintrack.repository.UserRepository;

@Service 
public class UserServiceImpl implements UserDetailsService {

    private final UserRepository userRepo;

    public UserServiceImpl(UserRepository userRepo){
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepo.findByUsername(username)
            .orElseThrow(
                () -> new UsernameNotFoundException(username)
            );
            return User
                .withUsername(user.getUsername())
                .password(user.getPasswordHash())
                .authorities(List.of())
                .build();
    }
    
}
