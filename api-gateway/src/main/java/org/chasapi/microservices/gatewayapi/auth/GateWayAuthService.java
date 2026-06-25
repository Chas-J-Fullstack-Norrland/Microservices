package org.chasapi.microservices.gatewayapi.auth;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.chasapi.microservices.gatewayapi.auth.dto.LoginRequest;
import org.chasapi.microservices.gatewayapi.auth.dto.NewUserRequest;
import org.chasapi.microservices.gatewayapi.auth.dto.UserDetailsDTO;
import org.chasapi.microservices.servicesecurity.JwtTokenGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class GateWayAuthService {

    private final UserClient userClient;

    public Mono<ResponseEntity<String>> login(LoginRequest request) {

        return userClient.fetchDetailsForUserName(request.username())
                .map(user -> {

                    if (!Objects.equals(user.password(), request.password())) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body("Wrong Password or Username");
                    }

                    return ResponseEntity.ok("Login successful");
                })
                .onErrorResume(e -> {
                    if (e instanceof org.springframework.web.reactive.function.client.WebClientResponseException.NotFound) {
                        return Mono.just(
                                ResponseEntity.status(HttpStatus.NOT_FOUND)
                                        .body("Could not find")
                        );
                    }

                    return Mono.just(
                            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                    .body("Error")
                    );
                });
    }

    public Mono<ResponseEntity<String>> register(NewUserRequest request) {

        return userClient.registerUser(request)
                .map(user -> ResponseEntity.status(HttpStatus.CREATED).body(user.username()))
                .onErrorResume(e -> Mono.just(
                        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage())
                ));
    }

}
