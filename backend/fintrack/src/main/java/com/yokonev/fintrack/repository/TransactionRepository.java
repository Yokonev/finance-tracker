package com.yokonev.fintrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.yokonev.fintrack.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{
    
}
