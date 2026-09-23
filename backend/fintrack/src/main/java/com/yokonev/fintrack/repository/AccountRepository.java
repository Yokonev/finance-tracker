package com.yokonev.fintrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.yokonev.fintrack.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long>{
    
}
