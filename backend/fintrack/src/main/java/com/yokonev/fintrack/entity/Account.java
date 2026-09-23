package com.yokonev.fintrack.entity;

import com.yokonev.fintrack.entity.value.Money;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Entity POJO for an Account in the database
 */
@Entity 
@Table(
    indexes = {
        @Index(name = "idx_account_owner", columnList = "user_id"),
    }
)
public class Account {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded 
    private Money amount;

    @Column(nullable = false)
    private String accountName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User owner;

    protected Account(){ //Required by JPA

    }

    public Account(
        @Nonnull Money amount, 
        @Nonnull String accountName)
    {
        this.amount = amount;
        this.accountName = requireNotBlank(accountName, "name");
    }

    public Long getId(){ return this.id; }
    public Money getAmount(){ return this.amount; }
    public String getAccountName(){ return this.accountName; }

    public void setAmount(@Nonnull Money newAmount){
        this.amount = newAmount;
    }

    public void rename(@Nonnull String newAccountName){
        this.accountName = requireNotBlank(newAccountName, "name");
    }

    private static String requireNotBlank(@Nonnull String value, @Nonnull String field) {
        if (value.isBlank()) {
            throw new IllegalArgumentException("An account " + field + " cannot be blank");
        }
        return value;
    }
    

}
