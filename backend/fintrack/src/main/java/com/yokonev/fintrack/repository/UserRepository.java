package com.yokonev.fintrack.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.yokonev.fintrack.entity.AppUser;

public interface UserRepository extends JpaRepository<AppUser, Long>{

    Optional<AppUser> findByEmail(String email);
    
    boolean existsByUsername(String username);

}