package org.chasapi.microservices.userservice.dto;



public record UserResponse(
        Long id,
        String username,
        String role
) {}