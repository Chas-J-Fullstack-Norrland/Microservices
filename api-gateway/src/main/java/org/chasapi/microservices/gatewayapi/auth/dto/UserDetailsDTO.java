package org.chasapi.microservices.gatewayapi.auth.dto;

import lombok.Builder;

@Builder
public record UserDetailsDTO (String username, String password, String role){
}
