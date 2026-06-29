package org.chasapi.microservices.userservice.dto;


import org.chasapi.microservices.userservice.model.User;

public record UserResponse(
        Long id,
        String username,
        String role
) {}