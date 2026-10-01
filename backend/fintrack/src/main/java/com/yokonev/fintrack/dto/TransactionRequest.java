package com.yokonev.fintrack.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.yokonev.fintrack.entity.value.TransactionType;

/**
 * <p> Request DTO for a transaction. </p>
 * 
 * <p> This will mainly be used to create and update a transaction. </p>
 * 
 * @param type
 * @param sourceAccountId 
 * @param destinationAccountId
 * @param amount
 * @param currency 
 * @param name
 * @param category
 * @param occurredAt
 */
public record TransactionRequest(
    TransactionType type,
    Long sourceAccountId,
    Long destinationAccountId,
    BigDecimal amount,
    String currency,
    String name,
    String category,
    Instant occurredAt
) {}
