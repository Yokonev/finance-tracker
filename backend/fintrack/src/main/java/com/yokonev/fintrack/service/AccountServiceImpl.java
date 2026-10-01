package com.yokonev.fintrack.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yokonev.fintrack.dto.AccountResponse;

@Service 
public class AccountServiceImpl implements AccountService {

    @Override
    public AccountResponse getAccountById(Long userId, Long accountId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAccountById'");
    }

    @Override
    public List<AccountResponse> getAccountsByUser(Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAccountsByUser'");
    }

    @Override
    public AccountResponse createAccount(Long userId, String accountName, String startBalance, String currency) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createAccount'");
    }

    @Override
    public AccountResponse updateAccountName(Long userId, Long accountId, String newName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateAccountName'");
    }

    @Override
    public AccountResponse updateAccountBalance(Long userId, String newBalance) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateAccountBalance'");
    }

    @Override
    public AccountResponse updateAccountCurrency(Long userId, String newCurrency) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateAccountCurrency'");
    }

    @Override
    public void deleteAccount(Long userId, Long accountId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteAccount'");
    }
    
}
