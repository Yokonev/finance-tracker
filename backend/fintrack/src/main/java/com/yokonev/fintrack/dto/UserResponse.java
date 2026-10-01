package com.yokonev.fintrack.dto;

/**
 * <p> Response DTO for an user </p>
 * 
 * @param id
 * @param username
 * @param email
 */
public record UserResponse(
    Long id,
    String username,
    String email
) {}