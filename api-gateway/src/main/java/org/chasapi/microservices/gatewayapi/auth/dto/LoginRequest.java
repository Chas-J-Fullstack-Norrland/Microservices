package org.chasapi.microservices.gatewayapi.auth.dto;

import lombok.Builder;

@Builder
public record LoginRequest(String username, String password){
}
