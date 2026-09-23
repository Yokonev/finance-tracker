package com.yokonev.fintrack.entity.value;

/**
 * Transaction type used to represent the impact on the balance of a user.
 * Used to know if we need to add or subtract on one balance or another
 */
public enum TransactionType {
    INCOME,
    EXPENSE,
    TRANSFER
}
