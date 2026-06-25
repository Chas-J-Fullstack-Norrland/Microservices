package org.chasapi.microservices.gatewayapi.auth;

import lombok.RequiredArgsConstructor;
import org.chasapi.microservices.gatewayapi.auth.dto.LoginRequest;
import org.chasapi.microservices.gatewayapi.auth.dto.NewUserRequest;
import org.chasapi.microservices.gatewayapi.auth.dto.UserDetailsDTO;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/")
public class GateWayAuthController {

    private final DiscoveryClient discoveryClient;

    @GetMapping("/services")
    public Object services() {
        return discoveryClient.getServices();
    }

    private final GateWayAuthService service;

    @PostMapping("/login")
    public Mono<ResponseEntity<String>> login(@RequestBody LoginRequest request) {
        return service.login(request);
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<String>> register(@RequestBody NewUserRequest request) {
        return service.register(request);
    }
}
