package com.yokonev.fintrack.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 
 * Entity POJO for a User in database.
 */
@Entity 
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String username;

    @Column(nullable=false, unique=true)
    private String email;

    @Column(nullable=false)
    private String passwordHash;

    protected User(){ //Required by JPA

    }

    public User(
        @Nonnull String username, 
        @Nonnull String email, 
        @Nonnull String passwordHash)
    {
        this.username = requireNotBlank(username, "username");
        this.email = requireNotBlank(email, "email");
        this.passwordHash = requireNotBlank(passwordHash, "password hash");
    }

    public Long getId(){ return this.id; }
    public String getUsername(){ return this.username; }
    public String getEmail(){ return this.email; }
    public String getPasswordHash(){ return this.passwordHash; }

    public void rename(@Nonnull String newUsername){
        this.username = requireNotBlank(newUsername, "username");
    }

    public void changeEmail(@Nonnull String newEmail){
        this.email = requireNotBlank(newEmail, "email");
    }

    public void changePasswordHash(@Nonnull String newPasswordHash){
        this.passwordHash = requireNotBlank(newPasswordHash, "password hash");
    }

    private static String requireNotBlank(@Nonnull String value, @Nonnull String field) {
        if (value.isBlank()) {
            throw new IllegalArgumentException("A " + field + " cannot be blank");
        }
        return value;
    }

}
