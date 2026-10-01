package com.yokonev.fintrack.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.yokonev.fintrack.entity.value.TransactionType;

/**
 * <p> Response DTO for a transaction </p>
 *
 * @param id
 * @param type
 * @param sourceAccountId
 * @param destinationAccountId
 * @param amount
 * @param currency
 * @param name
 * @param category
 * @param occurredAt
 */
public record TransactionResponse(
    Long id,
    TransactionType type,
    Long sourceAccountId,
    Long destinationAccountId,
    BigDecimal amount,
    String currency,
    String name,
    String category,
    Instant occurredAt
) {}
