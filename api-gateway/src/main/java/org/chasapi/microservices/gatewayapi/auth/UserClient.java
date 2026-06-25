package org.chasapi.microservices.gatewayapi.auth;

import org.chasapi.microservices.gatewayapi.auth.dto.NewUserRequest;
import org.chasapi.microservices.gatewayapi.auth.dto.UserDetailsDTO;
import reactor.core.publisher.Mono;

public interface UserClient {

    Mono<UserDetailsDTO> fetchDetailsForUserName(String username);

    Mono<UserDetailsDTO> registerUser(NewUserRequest request);
}