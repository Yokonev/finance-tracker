package com.yokonev.fintrack.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.yokonev.fintrack.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long>{

    Optional<Account> findByIdAndOwnerId(Long id, Long ownerId);

    List<Account> findAllByOwnerId(Long ownerId);

}
