package org.chasapi.microservices.gatewayapi.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.reactive.config.EnableWebFlux;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/webjars/**",
                                "/user-service/v3/api-docs",
                                "/user-service/v3/api-docs/**",
                                "/booking-service/v3/api-docs",
                                "/booking-service/v3/api-docs/**",
                                "/eureka/**",
                                "/actuator/**"
                        ).permitAll()
                        // All annan trafik kräver validerad JWT
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> {}) // Aktiverar JWT-validering mot secret-key i api-gateway.yml
                );

        return http.build();
    }
}
