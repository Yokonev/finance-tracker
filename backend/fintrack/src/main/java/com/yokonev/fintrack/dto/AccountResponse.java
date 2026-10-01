package com.yokonev.fintrack.dto;

import java.math.BigDecimal;

/**
 * <p> Answer DTO for a user's account. </p>
 * 
 * @param id identifier of the account
 * @param accountName name given to the specific account
 * @param balance current balance (with currency) of the account
 */
public record AccountResponse(
    Long id,
    String accountName,
    BigDecimal balance
) {}
