package com.yokonev.fintrack.dto;

/**
 * <p> Request DTO for an user </p>
 * 
 * @param username
 * @param email
 * @param password
 */
public record UserRequest(
    String username,
    String email,
    String password
) {}