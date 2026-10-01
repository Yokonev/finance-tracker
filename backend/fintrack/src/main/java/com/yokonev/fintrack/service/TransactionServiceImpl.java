package com.yokonev.fintrack.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yokonev.fintrack.dto.TransactionFilterRequest;
import com.yokonev.fintrack.dto.TransactionRequest;
import com.yokonev.fintrack.dto.TransactionResponse;

@Service 
public class TransactionServiceImpl implements TransactionService{

    @Override
    public List<TransactionResponse> getTransactionsByFilter(Long userId, TransactionFilterRequest filter) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTransactionsByFilter'");
    }

    @Override
    public TransactionResponse createTransaction(Long userId, TransactionRequest transaction) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createTransaction'");
    }

    @Override
    public TransactionResponse updateTransaction(Long userId, TransactionRequest transaction) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateTransaction'");
    }

    @Override
    public void deleteTransaction(Long userId, Long transactionId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteTransaction'");
    }
    
}
