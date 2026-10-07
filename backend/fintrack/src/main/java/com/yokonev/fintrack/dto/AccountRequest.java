package com.yokonev.fintrack.dto;

import java.math.BigDecimal;

/**
 * <p> Request DTO for a user's account. </p>
 * <p> Note: When making a query for specific accounts, the fields can be nullable. </p>
 * 
 * @param id identifier of the account
 * @param accountName name given to the specific account
 * @param balance current balance (with currency) of the account
 */
public record AccountRequest(
    Long id,
    String accountName,
    String currency,
    BigDecimal balance
) {}
