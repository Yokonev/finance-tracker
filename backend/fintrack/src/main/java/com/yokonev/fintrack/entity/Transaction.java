package com.yokonev.fintrack.entity;

import java.time.Instant;

import com.yokonev.fintrack.entity.value.Money;
import com.yokonev.fintrack.entity.value.TransactionType;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Id;


/**
 * A financial transaction recorded on an account.
 * <p>
 * The amount, type and date of a transaction are immutable. To correct them, the transaction
 * is deleted and a new one is created with the corrected values. The name and category can
 * be changed, as they describe the transaction without affecting its financial meaning.
 */
@Entity
@Table(
    indexes = {
        @Index(name = "idx_transactions_source", columnList = "source_account_id"),
        @Index(name = "idx_transactions_destination", columnList = "destination_account_id")
    }
)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_account_id", updatable = false)
    private Account source;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_account_id", updatable = false)
    private Account destination;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private TransactionType type;

    @Embedded
    private Money amount;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false, updatable = false)
    private Instant occurredAt;

    protected Transaction() {}

    private Transaction(
        Account source, 
        Account destination, 
        @Nonnull TransactionType type,
        @Nonnull Money amount, 
        @Nonnull String name, 
        @Nonnull String category, 
        @Nonnull Instant occurredAt) 
    {
        this.source = source;
        this.destination = destination;
        this.type = type;
        this.amount = amount;
        if (amount.getAmount().signum() <= 0) {
            throw new IllegalArgumentException("A transaction amount must be positive");
        }
        this.name = requireNotBlank(name, "name");
        this.category = requireNotBlank(category, "category");
        this.occurredAt = occurredAt;
    }

    public static Transaction expense(
        @Nonnull Account from, 
        @Nonnull Money amount,
        @Nonnull String name, 
        @Nonnull String category, 
        @Nonnull Instant occurredAt) 
    {
        return new Transaction(
            from, 
            null, 
            TransactionType.EXPENSE,
            amount, 
            name, 
            category, 
            occurredAt
        );
    }

    public static Transaction income(
        @Nonnull Account to, 
        @Nonnull Money amount,
        @Nonnull String name, 
        @Nonnull String category, 
        @Nonnull Instant occurredAt) 
    {
        return new Transaction(
            null, 
            to, 
            TransactionType.INCOME,
            amount, 
            name, 
            category, 
            occurredAt
        );
    }

    public static Transaction transfer(
        @Nonnull Account from, 
        @Nonnull Account to,
        @Nonnull Money amount,
        @Nonnull String name, 
        @Nonnull String category, 
        @Nonnull Instant occurredAt
    ) {
        if (from.getId().equals(to.getId())) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }
        return new Transaction(
            from, 
            to, 
            TransactionType.TRANSFER,
            amount, 
            name, 
            category, 
            occurredAt
        );
    }

    public void rename(@Nonnull String newName) {
        this.name = requireNotBlank(newName, "name");
    }

    public void changeCategory(@Nonnull String newCategory) {
        this.category = requireNotBlank(newCategory, "category");
    }

    public Long getId() { return id; }
    public Account getSourceAccount() { return source; }
    public Account getDestinationAccount() { return destination; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public Money getAmount() { return amount; }
    public TransactionType getType() { return type; }
    public Instant getOccurredAt() { return occurredAt; }

    private static String requireNotBlank(@Nonnull String value, @Nonnull String field) {
        if (value.isBlank()) {
            throw new IllegalArgumentException("A transaction " + field + " cannot be blank");
        }
        return value;
    }
}