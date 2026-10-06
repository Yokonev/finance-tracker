package com.yokonev.fintrack.service;

import java.util.List;

import com.yokonev.fintrack.dto.AccountResponse;

/**
 * 
 * <p> Account service interface. Follows CRUD operations. </p>
 */
public interface AccountService {
    
    AccountResponse getAccountById(Long userId, Long accountId);
    List<AccountResponse> getAccountsByUser(Long userId);

    AccountResponse createAccount(Long userId, String accountName, String startBalance, String currency);

    AccountResponse updateAccountName(Long userId, Long accountId, String newName);
    AccountResponse updateAccountBalance(Long userId, String newBalance);
    AccountResponse updateAccountCurrency(Long userId, String newCurrency);

    void deleteAccount(Long userId, Long accountId);

}
