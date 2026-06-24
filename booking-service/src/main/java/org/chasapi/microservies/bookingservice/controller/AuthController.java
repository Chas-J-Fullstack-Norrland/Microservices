package org.chasapi.microservies.bookingservice.controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.chasapi.microservies.bookingservice.security.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        if ("user".equals(username) && "pass".equals(password)) {
            return JwtUtil.generateToken(username);
        }
        throw new RuntimeException("Invalid credentials");
    }
}
