package org.chasapi.microservices.gatewayapi.auth.dto;

import lombok.Builder;

@Builder
public record NewUserRequest (String username, String password) {
}
