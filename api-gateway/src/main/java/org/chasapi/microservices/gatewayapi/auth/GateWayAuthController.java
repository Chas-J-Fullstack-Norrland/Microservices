package org.chasapi.microservices.gatewayapi.auth;

import lombok.RequiredArgsConstructor;
import org.chasapi.microservices.gatewayapi.auth.dto.LoginRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/")
public class GateWayAuthController {

    private final GateWayAuthService service;

    @GetMapping("/login")
    public String login(@RequestBody LoginRequest request){
        return service.login(request);
    }
}
