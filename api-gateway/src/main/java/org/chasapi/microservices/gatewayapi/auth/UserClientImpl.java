package org.chasapi.microservices.gatewayapi.auth;

import org.chasapi.microservices.gatewayapi.auth.dto.NewUserRequest;
import org.chasapi.microservices.gatewayapi.auth.dto.UserDetailsDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class UserClientImpl implements UserClient {

    private final WebClient webClient;

    public UserClientImpl(WebClient userServiceWebClient) {
        this.webClient = userServiceWebClient;
    }

    @Override
    public Mono<UserDetailsDTO> fetchDetailsForUserName(String username) {
        return webClient.get()
                .uri("/users/{username}", username)
                .retrieve()
                .bodyToMono(UserDetailsDTO.class);
    }

    @Override
    public Mono<UserDetailsDTO> registerUser(NewUserRequest request) {
        return webClient.post()
                .uri("/users/register")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(UserDetailsDTO.class);
    }
}