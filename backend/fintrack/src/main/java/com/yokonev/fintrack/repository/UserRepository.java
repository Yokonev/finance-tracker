package com.yokonev.fintrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.yokonev.fintrack.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

}