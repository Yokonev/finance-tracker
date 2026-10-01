package com.yokonev.fintrack.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

import com.yokonev.fintrack.entity.value.TransactionType;

/**
 * <p> Request DTO that holds the necessary query infos to query transactions. Any field can be nullable. </p>
 * 
 * <p> In the service layer, each set will have their elements OR-ed between them, and each field AND-ed between them. </p>
 * 
 * @param ids transaction ids
 * @param sources origin account
 * @param destinations target account
 * @param types type of the transaction
 * @param categories categories of the transaction
 * @param nameContains expression used to search for the name of a transaction
 * @param minAmount 
 * @param maxAmount
 * @param dateFrom
 * @param dateTo exclusive date
 */
public record TransactionFilterRequest(
        Set<Long> ids,
        Set<String> sources,
        Set<String> destinations,
        Set<TransactionType> types,
        Set<String> categories,
        String nameContains,
        BigDecimal minAmount,
        BigDecimal maxAmount,
        Instant dateFrom,
        Instant dateTo  
) {
    public TransactionFilterRequest {
        if (minAmount != null && maxAmount != null && minAmount.compareTo(maxAmount) > 0)
            throw new IllegalArgumentException("minAmount must be <= maxAmount");
        if (dateFrom != null && dateTo != null && dateFrom.isAfter(dateTo))
            throw new IllegalArgumentException("dateFrom must be before dateTo");
    }
}
