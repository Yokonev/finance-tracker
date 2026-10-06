package com.yokonev.fintrack.service;

import java.util.List;

import com.yokonev.fintrack.dto.TransactionFilterRequest;
import com.yokonev.fintrack.dto.TransactionRequest;
import com.yokonev.fintrack.dto.TransactionResponse;

/**
 * 
 * <p> Transaction service interface. Follows CRUD operations. </p>
 */
public interface TransactionService {

    List<TransactionResponse> getTransactionsByFilter(Long userId, TransactionFilterRequest filter);
    TransactionResponse createTransaction(Long userId, TransactionRequest transaction);
    TransactionResponse updateTransaction(Long userId, TransactionRequest transaction);
    void deleteTransaction(Long userId, Long transactionId);

}
